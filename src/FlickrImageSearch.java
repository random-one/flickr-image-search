
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;


public class FlickrImageSearch {

	// use your own API key
	public static final String apiKey = "860d4a9fa240ab0ef0c42eddc4e1c395";

	public static void main(String[] args) {

		String server = "www.flickr.com";
		REST rest;
		try {
			rest = new REST();
			rest.setHost(server);

			Flickr flickr = new Flickr(apiKey,rest);
			Flickr.debugStream = false;

			SearchParameters searchParams = new SearchParameters();
			searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);

			// enter search keywords
			String[] tags = new String[]{"qr code"};
			searchParams.setTags(tags);

			PhotosInterface photosInterface = flickr.getPhotosInterface();
			PhotoList photoList;
			// change the number of results per page
			final int perPage = 20;

			// this is just to find out the total results and pages
			photoList = photosInterface.search(searchParams, perPage, 0);
			int totalPages = photoList.getPages();
			int totalResults = photoList.getTotal();
			photoList.clear();

			File f = new File("images");
			f.mkdirs();

			for (int x = 0; x < totalPages; x++) {
				if (photoList != null){
					// retrieve each page
					photoList = photosInterface.search(searchParams, perPage, x);
					if (photoList != null) {
						for (int y = 0; y < photoList.size(); y++) {
							Photo photo = (Photo)photoList.get(y);
							System.out.println((x * perPage) + y + "/" + totalResults + " url: " + photo.getUrl());
							// save image to file
							BufferedImage image = photo.getLargeImage();
							if (image != null) {
								File outputfile = new File(f.getPath() + File.separator + photo.getId() + "." + photo.getOriginalFormat());
								ImageIO.write(image, photo.getOriginalFormat(), outputfile);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done");
	}
}
