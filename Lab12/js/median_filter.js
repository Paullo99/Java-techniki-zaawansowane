var JOptionPane = Java.type("javax.swing.JOptionPane");
var Color = Java.type("java.awt.Color");
var CopyOfBufferedImage = Java.type("com.example.imageConverter.CopyOfBufferedImage");

function process(originalImage) {

	var copy = CopyOfBufferedImage.getCopy(originalImage);
	var pixelsAround = parseInt(JOptionPane.showInputDialog("Liczba rozpatrywanych pixeli wokół:"));
	var arraySize =  Math.pow((2 * pixelsAround + 1), 2);
	var rValues = new Array(arraySize);
	var gValues = new Array(arraySize);
	var bValues = new Array(arraySize);
	var med_index = Math.floor(arraySize/2)

	for(i=pixelsAround; i<originalImage.getWidth() - pixelsAround; i++) 
    {
        for(j=pixelsAround; j<originalImage.getHeight() - pixelsAround; j++)
        {
			var counter = 0;
			//pętla analizująca otoczenia danego pixela
            for (k = -pixelsAround; k <= pixelsAround; k++) {
				for (l = -pixelsAround; l <= pixelsAround; l++) {
					pixel = new Color(originalImage.getRGB(i + k, j + l));
					rValues[counter] = pixel.getRed();
					gValues[counter] = pixel.getGreen();
					bValues[counter] = pixel.getBlue();
					counter++;
				}
			}

			rValues.sort(function(a, b){return a - b});
			gValues.sort(function(a, b){return a - b});
			bValues.sort(function(a, b){return a - b});

			copy.setRGB(i, j, new Color(rValues[med_index], gValues[med_index], bValues[med_index]).getRGB());

        }
    }

	return copy;
}

