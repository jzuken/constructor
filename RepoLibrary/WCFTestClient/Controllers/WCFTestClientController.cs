using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.ServiceModel;
using System.Web;
using System.Web.Mvc;

namespace WCFTestClient.Controllers
{
    public class WCFTestClientController : Controller
    {
        WCFTestReference.RepoLibraryClient wcfClient;
        public WCFTestClientController()
        {
            this.wcfClient = new WCFTestReference.RepoLibraryClient("WSHttpBinding_IRepoLibrary");
        }

        //
        // GET: /WCFTestClient/

        public ActionResult Index()
        {
            WCFTestReference.Project project = this.wcfClient.GetProject(0);
            ViewData["projectName"] = project.Name;
            return View();
        }

        public ActionResult AddProject(string projectId, string projectName)
        {
            int id = Convert.ToInt32(projectId);
            WCFTestReference.Project project = new WCFTestReference.Project();
            project.Id = id;
            project.Name = projectName;
            string result = wcfClient.SaveProject(project);
            if (result == "ok")
            {
                ViewData["projectId"] = id;
                ViewData["projectName"] = projectName;
            }
            else
            {
                RedirectToAction("Index");
            }
            return View();
        }

        public ActionResult GetProject(string projectId)
        {
            int id = Convert.ToInt32(projectId);
            WCFTestReference.Project project = wcfClient.GetProject(id);
            if (project != null)
            {
                ViewData["projectId"] = project.Id;
                ViewData["projectName"] = project.Name;
            }
            else
            {
                RedirectToAction("Index");
            }
            return View();
        }
    }
}
