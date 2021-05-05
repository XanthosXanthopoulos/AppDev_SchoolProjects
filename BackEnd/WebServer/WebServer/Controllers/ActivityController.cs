using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using NetTopologySuite.Geometries;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebServer.Data;
using WebServer.Models.Api.Response;
using WebServer.Models.Database;

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

        [HttpGet]
        [AllowAnonymous]
        public async Task<ApiResponse<ICollection<ActivityRespone>>> getActivities(double? latitude, double? longtitude, double? radius)
        {
            ApiResponse<ICollection<ActivityRespone>> apiResponse = new ApiResponse<ICollection<ActivityRespone>> { Response = new List<ActivityRespone>()};

            Point p = new Point(longtitude.GetValueOrDefault(), latitude.GetValueOrDefault()) { SRID = 4326 };

            ICollection<Activity> activities = _context.Activities.Where(a => a.Coordinates.Distance(p) < radius).ToList();

            foreach(Activity activity in activities)
            {
                apiResponse.Response.Add(new ActivityRespone
                {
                    ID = activity.ID,
                    Title = activity.Title,
                    Address = activity.Address,
                    Description = activity.Description
                });
            }

            return apiResponse;
        }

        [HttpGet]
        [AllowAnonymous]
        public async Task<ApiResponse<ICollection<ActivityRespone>>> Activities()
        {
            ApiResponse<ICollection<ActivityRespone>> apiResponse = new ApiResponse<ICollection<ActivityRespone>> { Response = new List<ActivityRespone>() };

            ICollection<Activity> activities = _context.Activities.Where(a => true).ToList();

            foreach (Activity activity in activities)
            {
                apiResponse.Response.Add(new ActivityRespone
                {
                    ContentType = "ACTIVITY",
                    ID = activity.ID,
                    Title = activity.Title,
                    Address = activity.Address,
                    Description = activity.Description
                });
            }

            return apiResponse;
        }

        [HttpGet]
        [Authorize]
        public async Task<ApiResponse<ICollection<PostResponseModel>>> Feed()
        {
            ApiResponse<ICollection<PostResponseModel>> apiResponse = new ApiResponse<ICollection<PostResponseModel>> { Response = new List<PostResponseModel>() };

            for (int i = 0; i < 3; ++i)
            {
                apiResponse.Response.Add(new PostResponseModel
                {
                    ContentType = "POST",
                    Username = "User " + i,
                    ProfileImageID = "p" + i,
                    ThumbnailImageID = "t" + i,
                    Description = "Post number " + i + " descritpion."
                });
            }

            return apiResponse;
        }
    }
}
