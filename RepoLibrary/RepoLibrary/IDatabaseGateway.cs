namespace RepoLibrary
{
    public interface IDatabaseGateway
    {
        Project GetProject(int id);

        string SaveProject(Project data);
    }
}
