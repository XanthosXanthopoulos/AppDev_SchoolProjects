using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.SignalR;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using WebServer.Data;
using WebServer.Models.Database;

namespace WebServer.Hubs
{
    [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
    public class NotificationsHub : Hub
    {
        private ApplicationDataDbContext _context;

        public NotificationsHub(ApplicationDataDbContext context)
        {
            _context = context;
        }

        public override Task OnConnectedAsync()
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;
            Groups.AddToGroupAsync(Context.ConnectionId, userID);

            return base.OnConnectedAsync();
        }

        public async Task Follow(string followeeID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            User user = await _context.Users.Include(u => u.Image).Where(u => u.UserID == userID).Select(u => u).FirstOrDefaultAsync();
            User followee = await _context.Users.Include(u => u.Image).Where(u => u.UserID == followeeID).Select(u => u).FirstOrDefaultAsync();

            Follow follow = new Follow();
            follow.FolloweeID = followeeID;
            follow.FollowerID = userID;
            follow.Accepted = followee.AccountType == Models.Enums.AccountType.Public ? true : false;
            follow.RequestTime = DateTime.Now;

            await _context.Follows.AddAsync(follow);
            await _context.SaveChangesAsync();

            if (followee.AccountType == Models.Enums.AccountType.Public)
            {
                await Clients.Groups(userID).SendAsync("acceptRequest", followeeID, followee.Name, followee.Image.ImageID);
            }
            else
            {
                await Clients.Groups(followeeID).SendAsync("followRequest", userID, user.Name, user.Image.ImageID);
            }
        }

        public async Task Accept(string followerID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            User user = await _context.Users.Include(u => u.Image).Where(u => u.UserID == userID).Select(u => u).FirstOrDefaultAsync();

            Follow follow = await _context.Follows.FindAsync(new string[] { userID, followerID });
            follow.Accepted = true;

            await _context.SaveChangesAsync();

            await Clients.Groups(followerID).SendAsync("acceptRequest", userID, user.Name, user.Image.ImageID);
        }

        public async Task Decline(string followerID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            Follow follow = await _context.Follows.FindAsync(new string[] { userID, followerID });
            _context.Remove(follow);

            await _context.SaveChangesAsync();
        }

        public async Task Unfollow(string followeeID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            Follow follow = await _context.Follows.FindAsync(new string[] { followeeID, userID});
            _context.Remove(follow);

            await _context.SaveChangesAsync();
        }

        public async Task Remove(string followerID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            Follow follow = await _context.Follows.FindAsync(new string[] { userID, followerID });
            _context.Remove(follow);

            await _context.SaveChangesAsync();
        }

        public async Task Cancel(string followeeID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            Follow follow = await _context.Follows.FindAsync(new string[] { followeeID, userID });
            _context.Remove(follow);

            await _context.SaveChangesAsync();
        }

        public async Task SendComment(int postID, string content)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            Post post = await _context.Posts.Include(p => p.Comments).Where(p => p.PostID == postID).FirstOrDefaultAsync();
            User user = await _context.Users.FindAsync(userID);
            Comment comment = new Comment { User = user, Content = content };

            _context.Comments.Add(comment);
            post.Comments.Add(comment);

            await _context.SaveChangesAsync();
        }

        public async Task SendLike(int postID)
        {
            string userID = Context.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            Post post = await _context.Posts.Include(p => p.Likes).Where(p => p.PostID == postID).FirstOrDefaultAsync();
            User user = await _context.Users.FindAsync(userID);
            Like like = new Like { User = user };

            _context.Likes.Add(like);
            post.Likes.Add(like);

            await _context.SaveChangesAsync();
        }
    }
}
