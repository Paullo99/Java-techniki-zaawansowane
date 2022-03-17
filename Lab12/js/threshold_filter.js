var Color = Java.type("java.awt.Color");

function process(originalImage){
    processedImage = originalImage;
    for(i=0; i<originalImage.getWidth(); i++) 
    {
        for(j=0; j<originalImage.getHeight(); j++)
        {
			var pixel = new Color(originalImage.getRGB(i,j))
            var r = pixel.getRed();
            var g = pixel.getGreen();
            var b = pixel.getBlue();
            var newColor = 0.299*r + 0.587*g + 0.114*b;
			
			if (newColor > 127){
				newPixel = 0xFFFFFF;
			}else{
				newPixel = 0x000000;
			}
            processedImage.setRGB(i,j,newPixel);
        }
    }

    return processedImage;
}