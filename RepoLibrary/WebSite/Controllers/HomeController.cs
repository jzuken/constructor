using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace WebSite.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            ViewBag.Message = "Modify this template to jump-start your ASP.NET MVC application.";

            return View();
        }

        public ActionResult About()
        {
            ViewBag.Message = "Your app description page.";

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }

        [Authorize]
        public ActionResult Constructor()
        {
            ViewBag.Message = "Constructor app";

            return View();
        }

        [Authorize]
        public ActionResult LoadShop()
        {
            RepoLibraryReference.RepoLibraryClient wcfClient = new RepoLibraryReference.RepoLibraryClient("WSHttpBinding_IRepoLibrary");
            string settings = wcfClient.GetProject("test").Settings;
            return Content(settings);
        }

        [Authorize]
        public ActionResult SaveShop(string settings)
        {
            RepoLibraryReference.RepoLibraryClient wcfClient = new RepoLibraryReference.RepoLibraryClient("WSHttpBinding_IRepoLibrary");
            RepoLibraryReference.Project project = new RepoLibraryReference.Project();
            project.Name = "test";
            project.Settings = settings;
            string res = wcfClient.SaveProject(project);
            if (res == "ok")
            {
                return Content("OK");
            }
            else
            {
                return Content("Fail");
            }
        }
    }
}
