using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace WebServer.Models.Api.Response
{
    public class ActivityResponse
    {
        public string ContentType { get; set; }

        public string ID { get; set; }

        public string Title { get; set; }

        public string Description { get; set; }

        public string Address { get; set; }

        public string Type { get; set; }

        public ICollection<string> Tags { get; set; }
    }
}
