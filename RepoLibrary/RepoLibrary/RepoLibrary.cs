using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using System.ComponentModel;
using System.ServiceModel;
using System.ServiceProcess;
using System.Configuration;
using System.Configuration.Install;

namespace RepoLibrary
{
    public class RepoLibraryWindowsService : ServiceBase
    {
        public ServiceHost serviceHost = null;
        public RepoLibraryWindowsService()
        {
            // Name the Windows Service
            ServiceName = "RepoLibraryWindowsService";
        }

        public static void Main()
        {
            ServiceBase.Run(new RepoLibraryWindowsService());
        }

        // Start the Windows service.
        protected override void OnStart(string[] args)
        {
            if (serviceHost != null)
            {
                serviceHost.Close();
            }

            // Create a ServiceHost for the CalculatorService type and 
            // provide the base address.
            serviceHost = new ServiceHost(typeof(RepoLibraryService));

            // Open the ServiceHostBase to create listeners and start 
            // listening for messages.
            serviceHost.Open();
        }

        protected override void OnStop()
        {
            if (serviceHost != null)
            {
                serviceHost.Close();
                serviceHost = null;
            }
        }
    }
}
