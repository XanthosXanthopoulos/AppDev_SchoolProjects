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
using WebServer.Models.Database;

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

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> FollowRequest([FromBody] string followee)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                if (!_context.Follows.Any(follow => follow.FolloweeID == followee))
                {
                    await _context.Follows.AddAsync(new Follow { FollowerID = userID, FolloweeID = followee, RequestTime = DateTime.Now, Accepted = false });
                    await _context.SaveChangesAsync();

                    //TODO: Implement notification system
                    //await _notificationsHub.Clients.User(followee).SendAsync("FollowRequest", userID);

                    return Ok();
                }
                else
                {
                    return StatusCode(StatusCodes.Status406NotAcceptable);
                }
            }
            else
            {
                return StatusCode(StatusCodes.Status401Unauthorized);
            }
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> FollowResponse([FromBody] FollowResponseModel response)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                Follow followRequest = await _context.Follows.FindAsync(userID, response.UserID);

                if (followRequest != null)
                {
                    if (response.Accepted)
                    {
                        followRequest.Accepted = true;
                    }
                    else
                    {
                        _context.Follows.Remove(followRequest);
                    }

                    await _context.SaveChangesAsync();

                    return Ok();
                }
                else
                {
                    return StatusCode(StatusCodes.Status404NotFound);
                }
            }
            else
            {
                return StatusCode(StatusCodes.Status401Unauthorized);
            }
        }

        [HttpGet]
        [Authorize]
        public async Task<List<FollowResponseModel>> GetFollowers()
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            List<FollowResponseModel> result = (from u in _context.Users
                                                from f in _context.Follows
                                                where f.FolloweeID == userID
                                                orderby f.Accepted
                                                select new FollowResponseModel { UserID = f.FollowerID, Accepted = f.Accepted }).ToList();

            return result;
        }

        [HttpGet]
        [Authorize]
        public async Task<List<FollowResponseModel>> GetFollowees()
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            List<FollowResponseModel> result = (from u in _context.Users
                                                from f in _context.Follows
                                                where f.FollowerID == userID
                                                orderby f.Accepted
                                                select new FollowResponseModel { UserID = f.FolloweeID, Accepted = f.Accepted }).ToList();

            return result;
        }
    }
}
