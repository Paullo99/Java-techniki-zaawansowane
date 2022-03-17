var Color = Java.type('java.awt.Color');

function process(originalImage){
	
    processedImage = originalImage;
    for(i=0; i<originalImage.getWidth(); i++) 
    {
        for(j=0; j<originalImage.getHeight(); j++)
        {
            var pixel = new Color(originalImage.getRGB(i,j))
            var newRed = 255 - pixel.getRed();
            var newGreen = 255 - pixel.getGreen();
            var newBlue = 255 - pixel.getBlue();

            processedImage.setRGB(i,j,new Color(newRed,newGreen,newBlue).getRGB());
        }
    }

    return processedImage;
}