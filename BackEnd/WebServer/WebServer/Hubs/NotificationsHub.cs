using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.SignalR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using WebServer.Data;

namespace WebServer.Hubs
{
    //[Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    public class NotificationsHub : Hub
    {
        private ApplicationDataDbContext _context;

        public NotificationsHub(ApplicationDataDbContext context)
        {
            _context = context;
        }

        public override Task OnConnectedAsync()
        {
            //Groups.AddToGroupAsync(Context.ConnectionId, Context.User.Identity.Name);

            Console.WriteLine(Context.ConnectionId + " Connected");

            return base.OnConnectedAsync();
        }

        public async Task Follow(string followeeID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;


            await Clients.Groups(followeeID).SendAsync("followRequest", userID);
        }

        public async Task Accept(string followerID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            await Clients.Groups(followerID).SendAsync("acceptRequest", userID);
        }

        public async Task Decline(string followerID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            await Clients.Groups(followerID).SendAsync("acceptRequest", userID);
        }

        public async Task Unfollow(string followeeID)
        {
            //No need to update
        }

        public async Task Remove(string followerID)
        {
            //No need to update
        }

        public async Task Cancel(string followeeID)
        {
            //No need to update
        }
    }
}
