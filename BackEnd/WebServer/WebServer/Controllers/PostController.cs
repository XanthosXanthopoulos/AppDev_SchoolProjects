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

            List<PostSummaryResponseModel> result = await (from p in _context.Posts
                                                    from f in _context.Follows
                                                    where f.FollowerID == userID && p.UserID == f.FolloweeID
                                                    orderby p.PostID descending
                                                    select new PostSummaryResponseModel 
                                                    { 
                                                        Username = f.Followee.Name, 
                                                        ProfileImageID = f.Followee.ProfileImage.ImageID, 
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

            HashSet<string> removable = new HashSet<string> { "a", "an", "the", "to", "from", "by", "then" };
            HashSet<string> searchTerms = null;
            Point point = null;

            if (query != null)
            {
                searchTerms = query.Split(" ").Where(t => !removable.Contains(t)).ToHashSet();
            }

            if (longtitude != null && latitude != null)
            {
                point = new Point(longtitude.GetValueOrDefault(), latitude.GetValueOrDefault()) { SRID = 4326 };
            }


            ApiResponse<ICollection<ActivityResponse>> apiResponse = new ApiResponse<ICollection<ActivityResponse>> { Response = new List<ActivityResponse>() };

            //ICollection<PostSummaryResponseModel> result = await (from p in _context.Posts
            //                                                      join f in _context.Follows on p.UserID equals f.FolloweeID
            //                                                      where f.FollowerID == userID
            //                                                      where country == null || p.Activities.Any(a => a.Country == (Country)country)
            //                                                      where city == null || p.Activities.Any(a => a.City.Equals(city, StringComparison.OrdinalIgnoreCase))
            //                                                      where p.Description.Split(" ", StringSplitOptions.RemoveEmptyEntries).Concat(p.Title.Split(" ", StringSplitOptions.RemoveEmptyEntries)).Any(d => searchTerms.Contains(d))
            //                                                      where point == null || radius == null || p.Activities.Any(a => a.Coordinates.Distance(point) < radius)
            //                                                      select ).ToListAsync();

            return new ApiResponse<ICollection<PostSummaryResponseModel>> { Response = null };
        }

        [HttpGet]
        [AllowAnonymous]
        public async Task<ApiResponse<PostResponseModel>> GetPost(int postID)
        {
            //PostResponseModel result = await (from p in _context.Posts
            //                                  where p.PostID == postID
            //                                  select new PostResponseModel
            //                                  {
            //                                      PostID = p.PostID,
            //                                      Title = p.Title,
            //                                      Description = p.Description,
            //                                      Date = p.Date,
            //                                      Username = p.User.Name,
            //                                      ProfileImageID = p.User.ProfileImage.ImageID,
            //                                      Images = p.Images.Select(i => i.ImageID).ToList(),
            //                                      Activities = p.Activities.Select(a => new ActivityResponse()
            //                                      {
            //                                          ID = a.ActivityID,
            //                                          Title = a.Title,
            //                                          Description = a.Description,
            //                                          Address = a.Address,
            //                                          Tags = a.Tags,
            //                                          Country = a.Country.ToString()

            //                                      }).ToList()
            //                                  })
            //                           .FirstAsync();


            PostResponseModel result = new PostResponseModel
            {
                PostID = postID,
                Title = "Title",
                Description = "Description",
                Date = new DateTime(2021, 7, 24),
                Username = "Username",
                ProfileImageID = "pfo_image",
                Images = new string[] { "im1", "im2" },
                Activities = new ActivityResponse[]
                {
                    new ActivityResponse
                    {
                        ID = "ActivityID",
                        Title = "ActTitle",
                        Description = "Description",
                        Address = "Address",
                        Tags = "a.Tags",
                        Country = Country.AD.ToString()
                    }
                }
            };

            return new ApiResponse<PostResponseModel> { Response = result };
        }

        [HttpPost]
        [AllowAnonymous]
        public async Task<IActionResult> UploadPost([FromBody] PostSubmitModel postSubmit)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            Post post = new Post
            {
                Title = postSubmit.Title,
                Description = postSubmit.Description,
                Date = postSubmit.Date,
                Images = postSubmit.Images.Select(i => new Image { ImageID = i }).ToList(),
                Activities = postSubmit.Activities.Select(a => _context.Activities.FirstOrDefault( ac => ac.ActivityID == a)).ToList(),
                User = _context.Users.Find(userID)
            };

            await _context.Posts.AddAsync(post);
            await _context.SaveChangesAsync();

            return Ok();
        }
    }
}
