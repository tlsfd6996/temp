package vision;

import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageSource;
import com.google.protobuf.ByteString;
import com.google.cloud.vision.v1.GcsDestination;
import com.google.cloud.vision.v1.GcsSource;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class GoogleVisionApiTester {
	
	public static void main(String[] args) {
		
		try {
	
			String imageFilePath = "C:\\Users\\admin\\Desktop\\Image\\text.jpg";
			String gcsPath = "http://223.195.109.38/image/text.jpg";
				
			List<AnnotateImageRequest> requests = new ArrayList<>();
		
			ByteString imgBytes = ByteString.readFrom(new FileInputStream(imageFilePath));
			
			ImageSource imgSource = ImageSource.newBuilder().setImageUri(gcsPath).build();
			
			//Image img = Image.newBuilder().setContent(imgBytes).build();
			
			Image img = Image.newBuilder().setSource(imgSource).build();
			
			Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
			requests.add(request);
		
			try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
				BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			    List<AnnotateImageResponse> responses = response.getResponsesList();
		
			    for (AnnotateImageResponse res : responses) {
			    	if (res.hasError()) {
			    		System.out.printf("Error: %s\n", res.getError().getMessage());
			    		return;
			    	}
		
			    	System.out.println("Text : ");
			    	System.out.println(res.getTextAnnotationsList().get(0).getDescription());
			      
			    	// For full list of available annotations, see http://g.co/cloud/vision/docs
			    	/*for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
				    	  
						//System.out.printf("Text: %s\n", annotation.getDescription());
						//System.out.printf("Position : %s\n", annotation.getBoundingPoly());
					}*/
			    }
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
