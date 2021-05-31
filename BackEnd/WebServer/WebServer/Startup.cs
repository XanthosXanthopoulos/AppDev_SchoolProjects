using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using NetTopologySuite.Geometries;
using Newtonsoft.Json.Linq;
using System;
using System.Net.Http;
using WebServer.Converters;
using WebServer.Data;
using WebServer.Hubs;
using WebServer.Models.Enums;
using WebServer.Services;
using WebServer.Utilities;

namespace WebServer
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddDbContext<ApplicationDbContext>(options => options.UseSqlServer(Configuration.GetConnectionString("DefaultConnection")));
            services.AddDbContext<ApplicationDataDbContext>(options => options.UseSqlServer(Configuration.GetConnectionString("ApplicationDataConnection"), x => x.UseNetTopologySuite()));

            services.AddIdentity<IdentityUser, IdentityRole>().AddEntityFrameworkStores<ApplicationDbContext>().AddDefaultTokenProviders();

            services.AddControllers().AddJsonOptions(options =>
            {
                options.JsonSerializerOptions.Converters.Add(new DateTimeConverter());
            });

            services.AddTokenAuthentication(Configuration);

            services.AddSignalR();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        [Obsolete]
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env, IServiceProvider serviceProvider)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseRouting();
            app.UseAuthentication();
            app.UseAuthorization();
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapHub<NotificationsHub>("/notifications");
                endpoints.MapControllers();
            });

            serviceProvider.GetService<ApplicationDbContext>().Database.EnsureCreated();
            if (serviceProvider.GetService<ApplicationDataDbContext>().Database.EnsureCreated())
            {
                ApplicationDataDbContext dbContext = serviceProvider.GetService<ApplicationDataDbContext>();
                Random random = new Random();

                string[] descriptions = 
                { 
                    "A lovely beer house.", 
                    "The Acropolis Museum, one of the most important museums in the world, houses the findings of only one archaeological site, the Athenian Acropolis.", 
                    "The Peace and Friendship Stadium, commonly known by its acronym SEF",
                    "The Ancient Agora of Athens is the best-known example of an ancient Greek agora and is situated to the northwest of Acropolis.",
                    "The top of Lycabettus Hill is the highest point of central Athens.",
                    "Just under the Acropolis, the neighborhood of Plaka gives visitors a taste of ‘old Athens’. It is the ideal place to take a walk or enjoy the local cafes and restaurants.",
                    "These are the 3 neoclassical buildings on Panepistimiou street, designed and built in the 19th century by Danish architect Theophil Hansen.",
                    "In Athens, all roads lead to Syntagma square.",
                    "The Temple of Olympian Zeus, right in the center of Athens, used to be the largest temple in Greece during the Roman times but today only a few columns remain standing."
                };

                string[] titles =
                {
                    "Arch beer house",
                    "Acropolis Museum",
                    "Peace and Friendship Stadium",
                    "The Ancient Agora of Athens is the best-known example of an ancient Greek agora and is situated to the northwest of Acropolis.",
                    "Lycabettus Hill",
                    "Plaka",
                    "Panepistimiou street",
                    "Syntagma square",
                    "Temple of Olympian Zeus"
                };

                string[] tags =
                {
                    "walking, nature, coffee",
                    "architecture, local food",
                    "beach, fine dining"
                };

                using (var httpClient = new HttpClient())
                {
                    for (int i = 0; i < 50; ++i)
                    {
                        Point point = new Point(23.727881 + (2 * random.NextDouble() - 1) * 0.039503, 37.983772 + (2 * random.NextDouble() - 1) * 0.04402) { SRID = 4326 };

                        using (var response = httpClient.GetAsync("https://api.opencagedata.com/geocode/v1/json?q=" + point.Y + "+" + point.X + "&key=255230665c9249b28259b49dacc2c198").Result)
                        {
                            string apiResponse = response.Content.ReadAsStringAsync().Result;
                            dynamic receivedReservation = JObject.Parse(apiResponse);

                            dynamic items = receivedReservation.results;
                            foreach (dynamic item in items)
                            {
                                Country country = EnumUtilities.GetValueFromDescription<Country>(Convert.ToString(item.components.country));

                                string city = "";
                                string address = "";

                                try
                                {
                                    city = Convert.ToString(item.components.city);
                                }
                                catch (Exception) { }

                                if (city == null || city.Length == 0)
                                {
                                    try
                                    {
                                        city = Convert.ToString(item.components.neighbourhood);
                                    }
                                    catch (Exception) { }
                                }

                                if (city == null || city.Length == 0)
                                {
                                    try
                                    {
                                        city = Convert.ToString(item.components.municipality);
                                    }
                                    catch (Exception) { }
                                }

                                try
                                {
                                    address = Convert.ToString(item.components.road) + Convert.ToString(item.components.house_number);
                                }
                                catch (Exception) { }

                                if (city == null) city = "";
                                if (address == null) address = "";

                                dbContext.Activities.Add(new Models.Database.Activity()
                                {
                                    Country = country,
                                    City = city,
                                    Address = address,
                                    Description = descriptions[random.Next(0, descriptions.Length)],
                                    Coordinates = point,
                                    Title = titles[random.Next(0, titles.Length)],
                                    Tags = tags[random.Next(0, tags.Length)]
                                });

                                break;
                            }
                        }
                    }
                }

                dbContext.Images.Add(new Models.Database.Image()
                {
                    ImageID = Guid.Empty.ToString()
                });

                dbContext.Images.Add(new Models.Database.Image()
                {
                    ImageID = "00000000-0000-0000-0000-000000000001"
                });

                dbContext.SaveChanges();
            }
        }
    }
}
