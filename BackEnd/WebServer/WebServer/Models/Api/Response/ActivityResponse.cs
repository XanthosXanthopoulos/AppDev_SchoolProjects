using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebServer.Models.Enums;

namespace WebServer.Models.Api.Response
{
    public class ActivityResponse
    {
        public string ID { get; set; }

        public string Title { get; set; }

        public string Description { get; set; }

        public string Address { get; set; }

        public string Country { get; set; }

        public string City { get; set; }

        public string Tags { get; set; }

        public double Latitude { get; set; }

        public double Longtitude { get; set; }
    }
}
