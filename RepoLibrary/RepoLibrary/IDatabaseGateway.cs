namespace RepoLibrary
{
    public interface IDatabaseGateway
    {
        Project GetProject(string id);

        string SaveProject(Project data);
    }
}
