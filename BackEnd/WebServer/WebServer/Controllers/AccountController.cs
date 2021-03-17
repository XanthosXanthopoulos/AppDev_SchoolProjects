using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System;
using System.Threading.Tasks;
using WebServer.Models.Api.Request;
using WebServer.Models.Api.Response;
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

        public AccountController(IConfiguration config, UserManager<IdentityUser> userManager, SignInManager<IdentityUser> signInManager)
        {

            this.userManager = userManager;
            this.signInManager = signInManager;

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
    }
}
