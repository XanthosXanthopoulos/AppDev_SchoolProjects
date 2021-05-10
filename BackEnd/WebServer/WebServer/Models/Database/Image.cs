using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace WebServer.Models.Database
{
    public class Image
    {
        [Key]
        public string ImageID { get; set; }
    }
}
