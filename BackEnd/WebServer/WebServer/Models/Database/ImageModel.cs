using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models.Database
{
    public class ImageModel
    {
        [Key]
        public string ImageID { get; set; }

        public string Album { get; set; }

        public string Name { get; set; }

        public string Location { get; set; }

        public bool IsProfile { get; set; }

        [ForeignKey("User")]
        public string UserID { get; set; }

        public UserModel User { get; set; }
    }
}
