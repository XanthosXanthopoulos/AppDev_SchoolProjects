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
    public class ActivityController : Controller
    {
        private readonly ApplicationDataDbContext _context;

        public ActivityController(ApplicationDataDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [AllowAnonymous]
        public async Task<ApiResponse<string>> UploadActivity([FromBody] ActivitySubmitModel activitySubmit)
        {
            Activity activity = new Activity
            {
                Address = activitySubmit.Address,
                City = activitySubmit.City,
                Country = activitySubmit.Country,
                Description = activitySubmit.Description,
                Tags = activitySubmit.Tags,
                Title = activitySubmit.Title,
                Coordinates = new Point(activitySubmit.Longtitude, activitySubmit.Latitude) { SRID = 4326 }
            };

            await _context.Activities.AddAsync(activity);
            await _context.SaveChangesAsync();

            return new ApiResponse<string> { Response = activity.ActivityID };
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ICollection<ActivityResponse>>> SearchActivities(string? query, int? country, string? city, double? latitude, double? longtitude, double? radius)
        {
            Point p = null;

            if (longtitude != null && latitude != null)
            {
                p = new Point(longtitude.GetValueOrDefault(), latitude.GetValueOrDefault()) { SRID = 4326 };
            }

            List<ActivityResponse> result = await _context.Activities.Where(a => country == null || a.Country == (Country)country)
                                                                      .Where(a => city == null || a.City == city)
                                                                      .Where(a => p == null || radius == null || a.Coordinates.Distance(p) < radius)
                                                                      .Where(a => query == null || EF.Functions.FreeText(a.Description, query) || EF.Functions.FreeText(a.Tags, query))
                                                                      .Select(a => new ActivityResponse
                                                                      {
                                                                          ID = a.ActivityID,
                                                                          Title = a.Title,
                                                                          Address = a.Address,
                                                                          Description = a.Description,
                                                                          Tags = a.Tags,
                                                                          Country = a.Country.ToString(),
                                                                          City = a.City,
                                                                          Latitude = a.Coordinates.Y,
                                                                          Longtitude = a.Coordinates.X
                                                                      }).ToListAsync();

            return new ApiResponse<ICollection<ActivityResponse>> { Response = result };
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ICollection<ActivityResponse>>> GetActivities(int? postID)
        {
            ICollection<Activity> result = _context.Posts.Include(p => p.PostActivities).ThenInclude(pa => pa.Activity).Where(p => p.PostID == postID).Select(p => p.PostActivities).First().Select(pa => pa.Activity).ToList();

            return new ApiResponse<ICollection<ActivityResponse>>
            {
                Response = result.Select(a => new ActivityResponse
                {
                    ID = a.ActivityID,
                    Address = a.Address,
                    City = a.City,
                    Country = a.Country.ToString(),
                    Description = a.Description,
                    Tags = a.Tags,
                    Title = a.Title,
                    Latitude = a.Coordinates.Y,
                    Longtitude = a.Coordinates.X
                })
                .ToList()
            };
        }
    }
}
