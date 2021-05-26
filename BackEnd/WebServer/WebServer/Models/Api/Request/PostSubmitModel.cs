using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using WebServer.Models.Enums;

namespace WebServer.Models.Api.Request
{
    public class PostSubmitModel
    {
        public string Title { get; set; }

        public string Description { get; set; }

        public DateTime Date { get; set; }

        public ICollection<string> Images { get; set; }

        public ICollection<string> Activities { get; set; }
    }
}
