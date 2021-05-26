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
        public async Task<ApiResponse<ICollection<PostSummaryResponseModel>>> GetPostSummaries()
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            var followees = from f in _context.Follows where f.FollowerID == userID select f;

            List<PostSummaryResponseModel> result = await (from p in _context.Posts
                                                           join f in followees
                                                           on p.UserID equals f.FolloweeID
                                                           orderby p.PostID descending
                                                           select new PostSummaryResponseModel 
                                                           { 
                                                               PostID = p.PostID,
                                                               Username = f.Followee.Name, 
                                                               ProfileImageID = f.Followee.Image.ImageID, 
                                                               ThumbnailImageID = p.Images.First().ImageID 
                                                           })
                                                           .ToListAsync();

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
                                                  Activities = p.Activities.Select(a => new ActivityResponse()
                                                  {
                                                      ID = a.ActivityID,
                                                      Title = a.Title,
                                                      Description = a.Description,
                                                      Address = a.Address,
                                                      Tags = a.Tags,
                                                      Country = a.Country.ToString()

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

            List<Image> images = _context.Images.Where(i => postSubmit.Images.Contains(i.ImageID)).ToList();

            Post post = new Post
            {
                Title = postSubmit.Title,
                Description = postSubmit.Description,
                Date = postSubmit.Date,
                Images = images,
                Activities = postSubmit.Activities.Select(a => _context.Activities.FirstOrDefault( ac => ac.ActivityID == a)).ToList(),
                User = await _context.Users.FindAsync(userID)
            };

            await _context.Posts.AddAsync(post);
            await _context.SaveChangesAsync();

            return new ApiResponse<bool> { Response = true };
        }
    }
}
