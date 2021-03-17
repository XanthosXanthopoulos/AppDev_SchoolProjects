using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace WebServer.Models.Database
{
    public class UserModel
    {
        [Key]
        public string UserID { get; set; }

        public string Name { get; set; }

        public string Surname { get; set; }

        public string Description { get; set; }

        public ICollection<ImageModel> Images { get; set; }
    }
}
