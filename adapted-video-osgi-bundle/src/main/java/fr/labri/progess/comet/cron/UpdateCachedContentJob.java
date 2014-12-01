package fr.labri.progess.comet.cron;

import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import fr.labri.progess.comet.model.Content;
import fr.labri.progess.comet.model.ContentWrapper;

public class UpdateCachedContentJob implements Job {

	Client client = ClientBuilder.newClient();

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {

		@SuppressWarnings("unchecked")
		final ConcurrentMap<String, Content> content = (ConcurrentMap<String, Content>) context
				.getJobDetail().getJobDataMap().get("content-cache");
		WebTarget target = client.target("http://localhost:8082").path("api")
				.path("content");
		ContentWrapper wrapper = target.request(MediaType.APPLICATION_XML).get(
				ContentWrapper.class);

		content.clear();
		for (Content con : wrapper.getContents()) {
			content.put(con.getUri(), con);
		}

	}

}