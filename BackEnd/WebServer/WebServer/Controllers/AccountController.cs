using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System;
using System.Security.Claims;
using System.Threading.Tasks;
using System.Linq;
using WebServer.Data;
using WebServer.Models.Api.Request;
using WebServer.Models.Api.Response;
using WebServer.Models.Database;
using WebServer.Services;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace WebServer.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class AccountController : ControllerBase
    {
        private readonly UserManager<IdentityUser> userManager;
        private readonly SignInManager<IdentityUser> signInManager;
        private readonly JwtService jwt;
        private readonly ApplicationDataDbContext _context;

        public AccountController(IConfiguration config, UserManager<IdentityUser> userManager, SignInManager<IdentityUser> signInManager, ApplicationDataDbContext context)
        {

            this.userManager = userManager;
            this.signInManager = signInManager;
            _context = context;

            jwt = new JwtService(config);
        }

        [HttpPost]
        [AllowAnonymous]
        public async Task<ApiResponse<AuthenticationResponseModel>> Register([FromBody] RegisterCredentialsModel userRegistrationRequest)
        {
            if (ModelState.IsValid)
            {
                IdentityUser result = await userManager.FindByEmailAsync(userRegistrationRequest.Email);

                if (result == null)
                {
                    IdentityUser user = new IdentityUser
                    {
                        UserName = userRegistrationRequest.Username,
                        NormalizedUserName = userRegistrationRequest.Username.ToLower(),
                        Email = userRegistrationRequest.Email
                    };

                    IdentityResult createResult = await userManager.CreateAsync(user, userRegistrationRequest.Password);

                    if (createResult.Succeeded)
                    {
                        await _context.Users.AddAsync(new User { UserID = user.Id, Name = user.UserName });
                        await _context.SaveChangesAsync();

                        return new ApiResponse<AuthenticationResponseModel> 
                        { 
                            Response = new AuthenticationResponseModel 
                            { 
                                Username = user.UserName, 
                                JWToken = jwt.GenerateSecurityToken(user.Email, user.Id)
                            } 
                        };
                    }
                    else
                    {
                        foreach (IdentityError error in createResult.Errors)
                        {
                            ModelState.AddModelError("Message", error.Description);
                        }

                        //TODO: Action to do when register fails
                    }
                }
                else
                {
                    ModelState.AddModelError("message", "Email already exists.");

                    return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "Email already exists." };
                }
            }

            return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "Invalid model state" };
        }

        [HttpPost]
        [AllowAnonymous]
        public async Task<ApiResponse<AuthenticationResponseModel>> Login([FromBody] SingInCredentialsModel userLoginRequest)
        {
            if (ModelState.IsValid)
            {
                IdentityUser user = await userManager.FindByEmailAsync(userLoginRequest.Email);

                if (await userManager.CheckPasswordAsync(user, userLoginRequest.Password) == false)
                {
                    ModelState.AddModelError("message", "Invalid credentials");

                    //TODO: Action to do when invalid credentials
                    return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "Invalid credentials" };
                }

                Microsoft.AspNetCore.Identity.SignInResult result = await signInManager.CheckPasswordSignInAsync(user, userLoginRequest.Password, false);
                
                if (result.Succeeded)
                {
                    string JWToken = jwt.GenerateSecurityToken(user.Email, user.Id);

                    return new ApiResponse<AuthenticationResponseModel>
                    {
                        Response = new AuthenticationResponseModel
                        {
                            Username = user.UserName,
                            JWToken = JWToken,
                            ProfileImageID = _context.Users.Include(u => u.Image).Where(u => u.UserID == user.Id).First().Image.ImageID
                        } 
                    };
                }
                else if (result.IsLockedOut)
                {
                    return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "This account is locked." };
                }
                else if (result.IsNotAllowed)
                {
                    return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "Sing in is prohibited." };
                }
                else
                {
                    ModelState.AddModelError("message", "Invalid login attempt");

                    return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "Invalid login attempt" };
                }
            }

            //TODO: Action to do when model is invalid
            return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "Invalid model state" };
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> Delete([FromBody] SingInCredentialsModel credentials)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                IdentityUser user = await userManager.FindByIdAsync(userID);
                User userInfo = await _context.Users.FindAsync(userID);

                ICollection<Follow> follows = _context.Follows.Where(f => f.FolloweeID == userID).ToList();
                _context.Follows.RemoveRange(follows);
                _context.Users.Remove(userInfo);

                await userManager.DeleteAsync(user);
                await _context.SaveChangesAsync();

                return Ok();
            }
            else
            {
                return StatusCode(StatusCodes.Status404NotFound);
            }
        }

        [HttpGet]
        [Authorize]
        public async Task<IActionResult> Logout()
        {
            await signInManager.SignOutAsync();

            return Ok();
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ProfileInfoResponseModel>> ProfileInfo()
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                User userInfo = await _context.Users.Where(u => u.UserID == userID).Include(i => i.Image).FirstAsync();

                return new ApiResponse<ProfileInfoResponseModel>
                {
                    Response = new ProfileInfoResponseModel
                    {
                        Name = userInfo.Name,
                        Surname = userInfo.Surname,
                        Birthday = userInfo.Birthday,
                        Description = userInfo.Description,
                        AccountType = userInfo.AccountType,
                        ProfileImageID = userInfo.Image.ImageID,
                        Country = userInfo.Country.ToString()
                    } 
                };
            }
            else
            {
                return new ApiResponse<ProfileInfoResponseModel> { ErrorMessage = "User info not found" };
            }
        }

        [HttpPost]
        [Authorize]
        public async Task<ApiResponse<AuthenticationResponseModel>> UpdateProfile([FromBody] ProfileInfoRequestModel profileInfo)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                User userInfo = await _context.Users.FindAsync(userID);

                userInfo.Name = profileInfo.Name;
                userInfo.Surname = profileInfo.Surname;
                userInfo.Birthday = profileInfo.Birthday;
                userInfo.Description = profileInfo.Description;
                userInfo.Country = profileInfo.Country;
                userInfo.AccountType = profileInfo.AccountType;

                if (profileInfo.ProfileImageID != null)
                {
                    Image im = await _context.Images.FindAsync(profileInfo.ProfileImageID);
                    userInfo.Image = im;
                }
                else if (profileInfo.ProfileImageID == null && userInfo.Image == null)
                {
                    Image im = await _context.Images.FindAsync(Guid.Empty.ToString());
                    userInfo.Image = im;
                }

                _context.Update(userInfo);
                await _context.SaveChangesAsync();

                return new ApiResponse<AuthenticationResponseModel> { Response = new AuthenticationResponseModel { ProfileImageID = userInfo.ImageID } };
            }
            else
            {
                return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "User not found" };
            }
        }
    }
}
