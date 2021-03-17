using System.ComponentModel.DataAnnotations;

namespace WebServer.Models.Api.Request
{
    public class ImageMetadataModel
    {
        [Required]
        public string ImageID { get; set; }

        public string Name { get; set; }

        public string Album { get; set; }
    }
}
