using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Database
{
    public class TravelPlanModel
    {
        [Key]
        public string ID { get; set; }

        public ICollection<ActivityModel> Activities { get; set; }
    }
}
