using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
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
    public class ActivityController : Controller
    {
        private readonly ApplicationDataDbContext _context;

        public ActivityController(ApplicationDataDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> UploadActivity([FromBody] ActivitySubmitModel activitySubmit)
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

            return Ok(activity.ActivityID);
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ICollection<ActivityResponse>>> SearchActivities(string? query, int? country, string? city, double? latitude, double? longtitude, double? radius)
        {
            HashSet<string> removable = new HashSet<string>{ "a", "an", "the", "to", "from", "by", "then" };
            HashSet<string> searchTerms = null;
            Point p = null;

            if (query != null)
            {
                searchTerms = query.Split(" ").Where(t => !removable.Contains(t)).ToHashSet();
            }

            if (longtitude != null && latitude != null)
            {
                p = new Point(longtitude.GetValueOrDefault(), latitude.GetValueOrDefault()) { SRID = 4326 };
            }


            ApiResponse<ICollection<ActivityResponse>> apiResponse = new ApiResponse<ICollection<ActivityResponse>> { Response = new List<ActivityResponse>()};

            ICollection<ActivityResponse> activities = _context.Activities.Where(a => country == null || a.Country == (Country)country)
                                                                  .Where(a => city == null || a.City.Equals(city, StringComparison.OrdinalIgnoreCase))
                                                                  .Where(a => searchTerms == null || searchTerms.Any(s => a.Tags.Contains(s)))
                                                                  .Where(a => p == null || radius == null || a.Coordinates.Distance(p) < radius)
                                                                  .Select(a => new ActivityResponse 
                                                                  {
                                                                      ContentType = "ACTIVITY",
                                                                      ID = a.ActivityID,
                                                                      Title = a.Title,
                                                                      Address = a.Address,
                                                                      Description = a.Description,
                                                                      Tags = a.Tags
                                                                  })
                                                                  .ToList();

            return new ApiResponse<ICollection<ActivityResponse>> { Response = activities };
        }
    }
}
