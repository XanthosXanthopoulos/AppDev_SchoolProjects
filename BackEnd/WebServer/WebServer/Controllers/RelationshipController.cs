using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using WebServer.Data;
using WebServer.Hubs;
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
        public async Task<IActionResult> Follow([FromBody] string followee)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (userID != null)
            {
                if (!_context.Follows.Any(follow => follow.FolloweeID == followee))
                {
                    await _context.Follows.AddAsync(new Follow { FollowerID = userID, FolloweeID = followee, RequestTime = DateTime.Now, Accepted = false });
                    await _context.SaveChangesAsync();

                    await _notificationsHub.Clients.User(followee).SendAsync("FollowRequest", userID);
                }
                else
                {
                    StatusCode(StatusCodes.Status406NotAcceptable);
                }
            }
            else
            {

            }

            return StatusCode(StatusCodes.Status401Unauthorized);
        }
    }
}
