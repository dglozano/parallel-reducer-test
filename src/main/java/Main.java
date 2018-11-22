import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        final int BATCH_SIZE = 40;

        List<Point> points = new ArrayList<Point>();
        for (double x = 0; x < 10; x += 0.05) {
            points.add(new MyPoint(x, Math.cos(x*x - 1)));
        }

        System.out.println("Initial size: " + points.size());
        System.out.println(points);

        List<List<Point>> batches = new ArrayList<>();

        // I calculate the amount of batches according to the predefined batch size
        int batchAmount = points.size() / BATCH_SIZE;
        for (int i = 0; i < batchAmount; i++) {
            int batchStart = i*BATCH_SIZE; //inclusive
            int batchEnd = i*BATCH_SIZE + BATCH_SIZE; //exclusive
            List<Point> batch = points.subList(batchStart, batchEnd);
            batches.add(batch);
        }

        System.out.println("Batches amount: " + batchAmount);


        // If there is still some data left, I put it in another batch
        if (points.size() % BATCH_SIZE != 0) {
            int batchStart = points.size() / BATCH_SIZE ; //inclusive
            int batchEnd = points.size(); //exclusive
            batches.add(points.subList(batchStart, batchEnd));
        }

        sequentialProcessing(points);
        parallelProcessingWithBatches(batches);
    }

    private static void parallelProcessingWithBatches(List<List<Point>> batches) {
        System.out.println("parallelProcessingWithBatches \n");
        Date start = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

        System.out.println("START TIME: " + sdf.format(start));
        //System.out.println("List of batches");
        //System.out.println(batches);

        List<Point> reducedList = batches.parallelStream() //Stream the Arrays of batches
                .flatMap(oneBatch -> SeriesReducer.reduce(oneBatch, 0.05).stream())
                .collect(Collectors.toList());

        System.out.println("Reduced list of points");
        System.out.println(reducedList);
        System.out.println("Size: " + reducedList.size());

        Date end = new Date();
        System.out.println("END TIME: " + sdf.format(end));
        System.out.println("ELAPSED TIME: " + (end.getTime() - start.getTime()) + " ms");
    }

    private static void sequentialProcessing(List<Point> data) {
        System.out.println("sequentialProcessing \n");
        Date start = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

        System.out.println("START TIME: " + sdf.format(start));
        //System.out.println("List of data");
        //System.out.println(data);

        List<Point> reducedList = SeriesReducer.reduce(data, 0.05);

        System.out.println("Reduced list of points");
        System.out.println(reducedList);
        System.out.println("Size: " + reducedList.size());

        Date end = new Date();
        System.out.println("END TIME: " + sdf.format(end));
        System.out.println("ELAPSED TIME: " + (end.getTime() - start.getTime()) + " ms");
    }
}