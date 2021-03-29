using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System;
using System.Security.Claims;
using System.Threading.Tasks;
using WebServer.Data;
using WebServer.Models.Api.Request;
using WebServer.Models.Api.Response;
using WebServer.Models.Database;
using WebServer.Services;

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
                        UserName = userRegistrationRequest.Name,
                        NormalizedUserName = userRegistrationRequest.Name.ToLower(),
                        Email = userRegistrationRequest.Email
                    };

                    IdentityResult createResult = await userManager.CreateAsync(user, userRegistrationRequest.Password);

                    if (createResult.Succeeded)
                    {
                        await _context.Users.AddAsync(new UserModel { UserID = user.Id, Name = user.UserName });
                        await _context.SaveChangesAsync();

                        return new ApiResponse<AuthenticationResponseModel> { Response = new AuthenticationResponseModel { Username = user.UserName, JWToken = jwt.GenerateSecurityToken(user.Email, user.Id) } };
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
                    return new ApiResponse<AuthenticationResponseModel> { Response = new AuthenticationResponseModel { Username = user.UserName, JWToken = jwt.GenerateSecurityToken(user.Email, user.Id) } };
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

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ProfileInfoResponseModel>> ProfileInfo()
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                IdentityUser user = await userManager.FindByIdAsync(userID);
                UserModel userInfo = await _context.Users.FindAsync(userID);

                return new ApiResponse<ProfileInfoResponseModel>
                {
                    Response = new ProfileInfoResponseModel
                    {
                        Username = user.UserName,
                        Email = user.Email,
                        Name = userInfo.Name,
                        Surname = userInfo.Surname,
                        Birthday = userInfo.Birthday,
                        Description = userInfo.Description,
                        AccountType = userInfo.AccountType
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
        public async Task<ApiResponse<AuthenticationResponseModel>> UpdateProfile([FromBody] ProfileInfoResponseModel profileInfo)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                IdentityUser user = await userManager.FindByIdAsync(userID);
                UserModel userInfo = await _context.Users.FindAsync(userID);

                if (!user.Email.Equals(profileInfo.Email, StringComparison.OrdinalIgnoreCase) && await userManager.FindByEmailAsync(profileInfo.Email) != null)
                {
                    return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "Email already exists" };
                }

                user.UserName = profileInfo.Username;
                user.Email = profileInfo.Email;

                userInfo.Name = profileInfo.Name;
                userInfo.Surname = profileInfo.Surname;
                userInfo.Birthday = profileInfo.Birthday;
                userInfo.Description = profileInfo.Description;
                userInfo.Country = profileInfo.Country;
                userInfo.AccountType = profileInfo.AccountType;

                await userManager.UpdateAsync(user);
                await _context.SaveChangesAsync();

                return new ApiResponse<AuthenticationResponseModel> { Response = new AuthenticationResponseModel { Username = profileInfo.Username } };
            }
            else
            {
                return new ApiResponse<AuthenticationResponseModel> { ErrorMessage = "User info not found" };
            }
        }
    }
}
