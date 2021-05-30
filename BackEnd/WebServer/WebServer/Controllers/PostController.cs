using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using NetTopologySuite.Geometries;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using WebServer.Data;
using WebServer.Models.Api.Request;
using WebServer.Models.Api.Response;
using WebServer.Models.Database;
using WebServer.Models.Enums;
using WebServer.Utilities;

namespace WebServer.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class PostController : Controller
    {
        private readonly ApplicationDataDbContext _context;

        public PostController(ApplicationDataDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ICollection<PostSummaryResponseModel>>> GetPostSummaries(bool? self)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            List<PostSummaryResponseModel> result;

            List<List<Activity>> activities;

            if (self.Value)
            {
                result = await (from p in _context.Posts
                                where p.UserID == userID
                                orderby p.PostID descending
                                select new PostSummaryResponseModel
                                {
                                    PostID = p.PostID,
                                    ThumbnailImageID = p.Images.First().ImageID,
                                    Title = p.Title,
                                    Date = p.Date,
                                    Likes = p.Likes.Count
                                })
                                .ToListAsync();

                activities = await (from p in _context.Posts
                                    where p.UserID == userID
                                    orderby p.PostID descending
                                    select p.Activities.ToList())
                                .ToListAsync();
            }
            else
            {
                var followees = from f in _context.Follows where f.FollowerID == userID && f.Accepted select f;

                result = await (from p in _context.Posts
                                join f in followees
                                on p.UserID equals f.FolloweeID
                                orderby p.PostID descending
                                select new PostSummaryResponseModel
                                {
                                    PostID = p.PostID,
                                    Username = f.Followee.Name,
                                    Title = p.Title,
                                    ProfileImageID = f.Followee.Image.ImageID,
                                    ThumbnailImageID = p.Images.First().ImageID,
                                    Date = p.Date
                                })
                                .ToListAsync();

                activities = await (from p in _context.Posts
                                    join f in followees
                                    on p.UserID equals f.FolloweeID
                                    orderby p.PostID descending
                                    select p.Activities.ToList())
                                .ToListAsync();
            }

            for(int i = 0; i < result.Count; ++i)
            {
                result[i].CountrySummary = activities[i].Select(a => a.Country.Description()).ToHashSet().Aggregate((sum, j) => sum + ", " + j);
            }

            return new ApiResponse<ICollection<PostSummaryResponseModel>> { Response = result };
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ICollection<PostSummaryResponseModel>>> SearchPosts(string? query, int? country, string? city, double? latitude, double? longtitude, double? radius)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            Point point = null;

            if (longtitude != null && latitude != null)
            {
                point = new Point(longtitude.GetValueOrDefault(), latitude.GetValueOrDefault()) { SRID = 4326 };
            }

            List<PostSummaryResponseModel> result = await (from p in _context.Posts
                                                           join f in _context.Follows on p.UserID equals f.FolloweeID
                                                           where f.FollowerID == userID
                                                           where country == null || p.Activities.Any(a => a.Country == (Country)country)
                                                           where city == null || p.Activities.Any(a => a.City == city)
                                                           where point == null || radius == null || p.Activities.Any(a => a.Coordinates.Distance(point) < radius)
                                                           where query == null || EF.Functions.FreeText(p.Description, query) || EF.Functions.FreeText(p.Title, query)
                                                           select new PostSummaryResponseModel
                                                           {
                                                               PostID = p.PostID,
                                                               Username = f.Followee.Name,
                                                               ProfileImageID = f.Followee.Image.ImageID,
                                                               ThumbnailImageID = p.Images.FirstOrDefault().ImageID
                                                           }).ToListAsync();

            return new ApiResponse<ICollection<PostSummaryResponseModel>> { Response = result };
        }

        [HttpGet]
        [AllowAnonymous]
        public async Task<ApiResponse<PostResponseModel>> GetPost(int postID)
        {
            PostResponseModel result = await (from p in _context.Posts
                                              where p.PostID == postID
                                              select new PostResponseModel
                                              {
                                                  PostID = p.PostID,
                                                  Title = p.Title,
                                                  Description = p.Description,
                                                  Date = p.Date,
                                                  Username = p.User.Name,
                                                  ProfileImageID = p.User.Image.ImageID,
                                                  Images = p.Images.Select(i => i.ImageID).ToList(),
                                                  Likes = p.Likes.Count,
                                                  Activities = p.Activities.Select(a => new ActivityResponse
                                                  {
                                                      ID = a.ActivityID,
                                                      Title = a.Title,
                                                      Description = a.Description,
                                                      Address = a.Address + ", " + a.City + ", " + a.Country.Description(),
                                                      Tags = a.Tags,
                                                      Country = a.Country.ToString()
                                                  }).ToList(),
                                                  Comments = p.Comments.Select(c => new CommentResponse
                                                  {
                                                      Username = c.User.Name,
                                                      ProfileImageID = c.User.ImageID,
                                                      Content = c.Content
                                                  }).ToList()
                                              })
                                       .FirstAsync();

            return new ApiResponse<PostResponseModel> { Response = result };
        }

        [HttpPost]
        [AllowAnonymous]
        public async Task<ApiResponse<bool>> UploadPost([FromBody] PostSubmitModel postSubmit)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            List<Image> images;
            if (postSubmit.Images == null || postSubmit.Images.Count == 0)
            {
                images = _context.Images.Where(i => i.ImageID == "00000000-0000-0000-0000-000000000001").ToList();
            }
            else 
            {
                images = _context.Images.Where(i => postSubmit.Images.Contains(i.ImageID)).ToList();
            }

            Post post = new Post
            {
                Title = postSubmit.Title,
                Description = postSubmit.Description,
                Date = postSubmit.Date,
                Images = images,
                Activities = postSubmit.Activities.Select(a => _context.Activities.FirstOrDefault(ac => ac.ActivityID == a)).ToList(),
                User = await _context.Users.FindAsync(userID),
                Likes = new List<Like>(),
                Comments = new List<Comment>()
            };

            await _context.Posts.AddAsync(post);
            await _context.SaveChangesAsync();

            return new ApiResponse<bool> { Response = true };
        }
    }
}
