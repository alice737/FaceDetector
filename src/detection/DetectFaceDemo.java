package detection;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.*;
import java.net.URLDecoder;

//
// Detects faces in an image, draws boxes around them, and writes the results
// to "faceDetection.png".
//
public class DetectFaceDemo {
    File f;

    public DetectFaceDemo(File f) {
        this.f = f;
    }

    public String run() {

        System.out.println("\nRunning DetectFaceDemo");

        // Create a face detector from the cascade file in the resources
        // directory.
        CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/resources/lbpcascade_frontalface.xml").getPath().substring(1));
        String url1 = f.getAbsolutePath();
        if (url1.startsWith("/", 0))
            url1 = url1.replaceFirst("/", "");
        try {
            url1 = URLDecoder.decode(url1, "UTF-8"); //this will replace %20 with spaces
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Mat image = Imgcodecs.imread(url1);
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        // Draw a bounding box around each face.
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }


        // Save the visualized detection.
        String filename = "D:\\FaceDetector\\src\\resources\\faceDetection.png";
        System.out.println(String.format("Writing %s", filename));
        Imgcodecs.imwrite(filename, image);
        return filename;
    }
}

