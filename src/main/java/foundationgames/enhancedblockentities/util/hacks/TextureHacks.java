package foundationgames.enhancedblockentities.util.hacks;

import com.mojang.blaze3d.platform.NativeImage;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Optional;

public enum TextureHacks {;
    @SuppressWarnings("null")
    public static Optional<byte[]> cropImage(@Nullable InputStream image, float u0, float v0, float u1, float v1) throws IOException {
        byte[] result = new byte[0];
        if (image != null) {
            try {
                NativeImage src = NativeImage.read(NativeImage.Format.RGBA, image);

                int w = src.getWidth();
                int h = src.getHeight();
                int x = (int) Math.floor(u0 * w);
                int y = (int) Math.floor(v0 * h);
                int sw = (int) Math.floor((u1 - u0) * w);
                int sh = (int) Math.floor((v1 - v0) * h);
                NativeImage prod = new NativeImage(src.format(), sw, sh, false);
                for (int u = 0; u < sw; u++) {
                    for (int v = 0; v < sh; v++) {
                        prod.setPixel(u, v, src.getPixel(x + u, y + v));
                    }
                }
                src.close();

                try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                     WritableByteChannel writableByteChannel = Channels.newChannel(byteArrayOutputStream)) {
                    if (!prod.writeToChannel(writableByteChannel)) {
                        throw new IOException("Could not write cropped image to byte array");
                    }

                    result = byteArrayOutputStream.toByteArray();
                }
                prod.close();
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
        return Optional.of(result);
    }
}
