using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RepoLibraryServiceWebRole
{
    public interface IDatabaseGateway
    {
        Project GetProject(int id);

        string SaveProject(Project data);
    }
}
