using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.StaticFiles;
using System;
using System.IO;
using System.Linq;
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
        public async Task<IActionResult> UploadFile([FromForm] IFormFile file)
        {
            if (ModelState.IsValid && file != null && file.ContentType.StartsWith("image"))
            {

                string fileID = Guid.NewGuid().ToString() + new FileExtensionContentTypeProvider().Mappings.FirstOrDefault(type => type.Value == file.ContentType).Key;

                string directory = Path.Combine(Environment.CurrentDirectory, "FileStorage");
                Directory.CreateDirectory(directory);

                var filePath = Path.Combine(directory, fileID);

                // Create a new file in the home-guac directory with the newly generated file name
                using (FileStream stream = new FileStream(filePath, FileMode.Create))
                {
                    //copy the contents of the received file to the newly created local file 
                    await file.CopyToAsync(stream);
                }

                // return the file name for the locally stored file
                return Ok(fileID);
            }
            else
            {
                return StatusCode(StatusCodes.Status415UnsupportedMediaType);
            }
        }

        [HttpPost]
        [Authorize]
        public async Task<IActionResult> UploadFileMetadata([FromBody] ImageMetadataModel image)
        {
            if (ModelState.IsValid)
            {
                string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

                await _context.Images.AddAsync(new ImageModel { UserID = userID, ImageID = image.ImageID, Name = image.Name, Album = image.Album });
                await _context.SaveChangesAsync();

                // return the file name for the locally stored file
                return Ok();
            }
            else
            {
                return StatusCode(StatusCodes.Status400BadRequest);
            }
        }

        [HttpGet]
        [Authorize]
        public async Task<IActionResult> Download(string id)
        {
            string userID = User.FindFirst(ClaimTypes.NameIdentifier)?.Value;

            if (_context.Images.Any(image => image.UserID == userID && image.ImageID == id))
            {
                string[] files = Directory.GetFiles(Path.Combine(Environment.CurrentDirectory, "FileStorage"), id + ".*");

                if (files.Length > 0)
                {
                    // Get all bytes of the file and return the file with the specified file contents 
                    byte[] data = await System.IO.File.ReadAllBytesAsync(files[0]);
                    if (new FileExtensionContentTypeProvider().TryGetContentType(files[0], out string mimeType))
                    {
                        return File(data, mimeType);
                    }
                    else
                    {
                        return File(data, "application/octet-stream");
                    }
                }
                else
                {
                    // since file does not exist remove it from database
                    ImageModel image = new ImageModel { ImageID = id };
                    _context.Images.Attach(image);
                    _context.Images.Remove(image);
                    await _context.SaveChangesAsync();

                    // return error if file not found
                    return StatusCode(StatusCodes.Status500InternalServerError);
                }
            }

            return StatusCode(StatusCodes.Status403Forbidden);
        }
    }
}
