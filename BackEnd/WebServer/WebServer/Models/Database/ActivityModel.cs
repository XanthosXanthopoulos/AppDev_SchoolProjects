using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Database
{
    public class ActivityModel
    {
        [Key]
        public string ID { get; set; }

        public string Description { get; set; }

        public DateTime ScheduledTime { get; set; }

        [ForeignKey("PointOfInterest")]
        public string PointOfInterestID { get; set; }
        public PointOfInterestModel PointOfInterest { get; set; }
    }
}
