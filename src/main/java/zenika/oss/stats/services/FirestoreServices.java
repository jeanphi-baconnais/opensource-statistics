package zenika.oss.stats.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import zenika.oss.stats.beans.ZenikaMember;
import zenika.oss.stats.beans.github.GitHubProject;
import zenika.oss.stats.beans.gcp.StatsContribution;
import zenika.oss.stats.config.FirestoreCollections;
import zenika.oss.stats.exception.DatabaseException;
import zenika.oss.stats.mapper.ZenikaMemberMapper;
import zenika.oss.stats.mapper.ZenikaProjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class FirestoreServices {

    @Inject
    Firestore firestore;

    /**
     * Create one member in the databse.
     * @param zMember the member to create.
     */
    public void createMember(ZenikaMember zMember) {
        createDocument(zMember, FirestoreCollections.MEMBERS.value, zMember.getId());
    }

    public List<ZenikaMember> getAllMembers() throws DatabaseException {
        CollectionReference zmembers = firestore.collection(FirestoreCollections.MEMBERS.value);
        ApiFuture<QuerySnapshot> querySnapshot = zmembers.get();
        try {
            return querySnapshot.get().getDocuments().stream()
                    .map(ZenikaMemberMapper::mapFirestoreZenikaMemberToZenikaMember).toList();
        } catch (InterruptedException | ExecutionException exception) {
            throw new DatabaseException(exception);
        }
    }

    /**
     * Remove stats for a GitHub account for a specific year.
     *
     * @param githubMember : GitHub login
     * @param year         : year to delete
     * @throws DatabaseException exception
     */
    public void deleteStatsForAGitHubAccountForAYear(String githubMember, int year) throws DatabaseException {
        CollectionReference zStats = firestore.collection(FirestoreCollections.STATS.value);
        Query query = zStats.whereEqualTo("githubHandle", githubMember).whereEqualTo("year", String.valueOf(year));
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            List<QueryDocumentSnapshot> stats = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : stats) {
                document.getReference().delete();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Save stats for a GitHub account for a specific month of a year.
     *
     * @param statsContribution : stats to save
     */
    public void saveStatsForAGitHubAccountForAYear(StatsContribution statsContribution) {
        List<ApiFuture<WriteResult>> futures = new ArrayList<>();
        futures.add(firestore.collection(FirestoreCollections.STATS.value).document().set(statsContribution));
    }

    /**
     * Delete all stats for the year in parameter.
     * @param year : the year that we want to remove stats
     * @throws DatabaseException exception
     */
    public void deleteStatsForAllGitHubAccountForAYear(int year) throws DatabaseException {
        CollectionReference zStats = firestore.collection(FirestoreCollections.STATS.value);
        Query query = zStats.whereEqualTo("year", String.valueOf(year));
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        try {
            List<QueryDocumentSnapshot> stats = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : stats) {
                document.getReference().delete();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Create a project in the Firestore database.
     * @param project the project to create.
     */
    public void createProject(GitHubProject project) {
        createDocument(project, FirestoreCollections.PROJECTS.value, project.getId());
    }

    /**
     * Retrieve all projects from the Firestore database.
     * @return a list of all projects.
     */
    public List<GitHubProject> getAllProjects() throws DatabaseException {
        CollectionReference zProjects = firestore.collection(FirestoreCollections.PROJECTS.value);
        ApiFuture<QuerySnapshot> querySnapshot = zProjects.get();
        try {
            return querySnapshot.get().getDocuments().stream().map(ZenikaProjectMapper::mapFirestoreZenikaProjectToGitHubProject).toList();
        } catch (InterruptedException | ExecutionException exception) {
            throw new DatabaseException(exception);
        }
    }

    /**
     * Remove all projects from the Firestore database.
     * @throws DatabaseException exception
     */
    public void deleteAllProjects() throws DatabaseException {
        deleteAllDocuments(FirestoreCollections.PROJECTS);
    }


    /**
     * Remove all members
     *
     * @throws DatabaseException exception
     */
    public void deleteAllMembers() throws DatabaseException {
        deleteAllDocuments(FirestoreCollections.MEMBERS);
    }

    /**
     * Create a document in the Firestore database.
     * @param document the document to create.
     * @param collectionPath the path of the collection in which to create the document.
     * @param documentId the id of the document to create.
     * @param <T> the type of the document to create.
     */
    public <T> void createDocument(T document, String collectionPath, String documentId) {
        List<ApiFuture<WriteResult>> futures = new ArrayList<>();
        futures.add(firestore.collection(collectionPath).document(documentId).set(document));
    }

    /**
     * Delete all documents in a specific Firestore collection.
     * @param collectionType the type of collection to delete.
     * @param <T> the type of document
     * @throws DatabaseException exception
     */
    public <T> void deleteAllDocuments(FirestoreCollections collectionType) throws DatabaseException {
        CollectionReference collection = firestore.collection(collectionType.value);
        ApiFuture<QuerySnapshot> querySnapshot = collection.get();
        try {
            List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                document.getReference().delete();
            }
        } catch (InterruptedException | ExecutionException exception) {
            throw new DatabaseException(exception);
        }
    }
}
