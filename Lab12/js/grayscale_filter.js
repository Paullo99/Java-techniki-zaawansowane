function process(originalImage){
    processedImage = originalImage;
    for(i=0; i<originalImage.getWidth(); i++) 
    {
        for(j=0; j<originalImage.getHeight(); j++)
        {
            var pixel = originalImage.getRGB(i,j)
            var r = (pixel >> 16) & 0xFF;
            var g = (pixel >> 8) & 0xFF;
            var b = (pixel >> 0) & 0xFF;
            var newColor = 0.299*r + 0.587*g + 0.114*b;
            var newPixel = (newColor << 16) | (newColor << 8) | (newColor << 0)
            processedImage.setRGB(i,j,newPixel);
        }
    }

    return processedImage;
}