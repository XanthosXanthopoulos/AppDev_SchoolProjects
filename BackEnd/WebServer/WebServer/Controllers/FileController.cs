using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.IO;
using System.Security.Claims;
using System.Threading.Tasks;
using WebServer.Data;
using WebServer.Models.Api.Request;
using WebServer.Models.Database;

namespace WebServer.Controllers
{
    [ApiController]
    [Route("api/[controller]/[action]")]
    public class FileController : Controller
    {
        private ApplicationDataDbContext _context;

        public FileController(ApplicationDataDbContext context)
        {
            _context = context;
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> UploadFile(IFormFile file)
        {
            string fileID = Guid.NewGuid().ToString();

            Directory.CreateDirectory("path");
            var filePath = Path.Combine("path", fileID);

            // Create a new file in the home-guac directory with the newly generated file name
            using (FileStream stream = new FileStream(filePath, FileMode.Create))
            {
                //copy the contents of the received file to the newly created local file 
                await file.CopyToAsync(stream);
            }

            // return the file name for the locally stored file
            return Ok(fileID);
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> UploadFileMetadata([FromBody] ImageMetadataModel image)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            await _context.Images.AddAsync(new ImageModel { UserID = userID, ImageID = image.ImageID, Album = image.Album });
            await _context.SaveChangesAsync();

            // return the file name for the locally stored file
            return Ok();
        }

        [HttpGet("{id}")]
        [Authorize]
        public async Task<IActionResult> Download(string id)
        {
            string path = Path.Combine("path", id);

            if (System.IO.File.Exists(path))
            {
                // Get all bytes of the file and return the file with the specified file contents 
                byte[] data = await System.IO.File.ReadAllBytesAsync(path);
                return File(data, "application/octet-stream");
            }

            else
            {
                // return error if file not found
                return StatusCode(StatusCodes.Status500InternalServerError);
            }
        }
    }
}
