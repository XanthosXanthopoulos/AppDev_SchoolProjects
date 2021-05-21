using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using WebServer.Data;
using WebServer.Hubs;
using WebServer.Models.Api.Request;
using WebServer.Models.Api.Response;
using WebServer.Models.Database;
using WebServer.Utilities;

namespace WebServer.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class RelationshipController : Controller
    {
        private readonly ApplicationDataDbContext _context;
        private readonly IHubContext<NotificationsHub> _notificationsHub;

        public RelationshipController(IHubContext<NotificationsHub> notificationsHub, ApplicationDataDbContext context)
        {
            _notificationsHub = notificationsHub;
            _context = context;
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<List<FollowResponseModel>>> GetFollowers()
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            List<FollowResponseModel> result = (from u in _context.Users
                                                join f in _context.Follows 
                                                on u.UserID equals f.FolloweeID
                                                where userID == f.FolloweeID
                                                orderby f.Accepted
                                                select new FollowResponseModel { UserID = f.FollowerID, Username = f.Follower.Name, ProfileImageID = f.Follower.Image.ImageID, Status = f.Accepted ? "FOLLOWED" : "PENDING_INCOMING" }).ToList();

            return new ApiResponse<List<FollowResponseModel>> { Response = result };
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<List<FollowResponseModel>>> GetFollowees()
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            List<FollowResponseModel> result = (from u in _context.Users
                                                join f in _context.Follows
                                                on u.UserID equals f.FollowerID
                                                where f.FollowerID == userID
                                                orderby f.Accepted
                                                select new FollowResponseModel { UserID = f.FolloweeID, Username = f.Followee.Name, ProfileImageID = f.Followee.Image.ImageID, Status = f.Accepted ? "FOLLOWING" : "PENDING_OUTCOMING" }).ToList();

            return new ApiResponse<List<FollowResponseModel>> { Response = result };
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<List<UserResponse>>> searchUsers(string query)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            List<UserResponse> result = await _context.Users.Where(u => u.UserID != userID).Where(u => u.Name.Contains(query)).Select(u => new UserResponse 
            { 
                Username = u.Name,
                UserID = u.UserID,
                ProfileImageID = u.Image.ImageID
            })
            .ToListAsync();
                     
            foreach (UserResponse user in result)
            {
                Follow followee = await _context.Follows.FindAsync(new string[] { user.UserID, userID });

                if (followee == null)
                {
                    user.Status = FollowStatus.NONE.ToString();
                }
                else if (followee.Accepted)
                {
                    user.Status = FollowStatus.FOLLOWING.ToString();
                }
                else
                {
                    user.Status = FollowStatus.PENDING_OUTCOMING.ToString();
                }
            }

            return new ApiResponse<List<UserResponse>> { Response = result };
        }
    }
}
