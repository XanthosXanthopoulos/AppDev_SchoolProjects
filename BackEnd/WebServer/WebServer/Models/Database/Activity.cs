using NetTopologySuite.Geometries;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using WebServer.Models.Api.Response;
using WebServer.Models.Enums;

namespace WebServer.Models.Database
{
    public class Activity
    {
        [Key]
        public string ActivityID { get; set; }

        public string Title { get; set; }

        public string Description { get; set; }

        public string Address { get; set; }

        public string Tags { get; set; }

        public Country Country { get; set; }

        public string City { get; set; }

        public Point Coordinates { get; set; }

        public ICollection<PostActivity> PostActivities { get; set; }

        public static implicit operator ActivityResponse(Activity activity)
        {
            return new ActivityResponse
            {
                ID = activity.ActivityID,
                Address = activity.Address,
                Country = activity.Country.ToString(),
                Description = activity.Description,
                Tags = activity.Tags,
                Title = activity.Title
            };
        }
    }
}
