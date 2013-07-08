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

    }
}
