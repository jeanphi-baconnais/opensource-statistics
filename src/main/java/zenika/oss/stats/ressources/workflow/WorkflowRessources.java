package zenika.oss.stats.ressources.workflow;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import zenika.oss.stats.beans.CustomStatsContributionsUserByMonth;
import zenika.oss.stats.beans.github.GitHubMember;
import zenika.oss.stats.exception.DatabaseException;
import zenika.oss.stats.mapper.ZenikaMemberMapper;
import zenika.oss.stats.services.FirestoreServices;
import zenika.oss.stats.services.GitHubServices;

import java.util.List;

@ApplicationScoped
@Path("/v1/workflow/")
public class WorkflowRessources {

    @Inject
    GitHubServices gitHubServices;

    @Inject
    FirestoreServices firestoreServices;

    @POST
    @Path("members/save")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveMembers() {

        List<GitHubMember> gitHubMembers = gitHubServices.getZenikaOpenSourceMembers();
        gitHubMembers.forEach(gitHubMember -> firestoreServices.createMember(ZenikaMemberMapper.mapGitHubMemberToZenikaMember(gitHubMember)));

        return Response.ok().build();
    }

    @POST
    @Path("forked-projects/save")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveForkedProject() {
        return Response.ok("\uD83D\uDEA7 Not implemented yet").build();
    }

    @POST
    @Path("personal-projects/save")
    @Produces(MediaType.TEXT_PLAIN)
    public Response savePersonalProjects() {
        return Response.ok("\uD83D\uDEA7 Not implemented yet").build();
    }

    @POST
    @Path("stats/save/{year}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveStatsForYear(@PathParam("year") int year) {

        return Response.ok("\uD83D\uDEA7 Not implemented yet").build();
    }

    @POST
    @Path("stats/save/{githubMember}/{year}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveStatsForAGitHubAccountForAYear(@PathParam("githubMember") String githubMember, @PathParam("year") int year) throws DatabaseException {

        firestoreServices.deleteStatsForAGitHubAccountForAYear(githubMember, year);

        List<CustomStatsContributionsUserByMonth> stats = gitHubServices.getContributionsForTheCurrentYear(githubMember, year);

        if (!stats.isEmpty()) {
            firestoreServices.saveStatsForAGitHubAccountForAYear(githubMember, year, stats);
        }

        return Response.ok().build();
    }

}
