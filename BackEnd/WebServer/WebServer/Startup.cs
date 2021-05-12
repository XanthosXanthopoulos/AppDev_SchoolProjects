using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System;
using System.Collections.Generic;
using WebServer.Converters;
using WebServer.Data;
using WebServer.Hubs;
using WebServer.Services;

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

                dbContext.Activities.Add(new Models.Database.Activity()
                {
                    Address = "1871  Poplar Lane",
                    Description = "A fake activity",
                    Coordinates = new NetTopologySuite.Geometries.Point(23.73543741130086, 37.9894702164558) { SRID = 4326 },
                    Title = "Activity 1"
                });

                dbContext.Activities.Add(new Models.Database.Activity()
                {
                    Address = "187 Lane",
                    Description = "A fake activity",
                    Coordinates = new NetTopologySuite.Geometries.Point(23.720947233378396, 37.982539628169874) { SRID = 4326 },
                    Title = "Activity 2"
                });

                dbContext.Activities.Add(new Models.Database.Activity()
                {
                    Address = "Poplar Lane",
                    Description = "A fake activity",
                    Coordinates = new NetTopologySuite.Geometries.Point(23.590729071505763, 38.083373506423285) { SRID = 4326 },
                    Title = "Activity 3"
                });

                dbContext.SaveChanges();
            }
        }
    }
}
