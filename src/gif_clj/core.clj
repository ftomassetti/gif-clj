(ns gif-clj.core
  (:import [com.github.gif AnimatedGifEncoder]))

(import java.io.ByteArrayOutputStream)

(defn write-gif [filename imgs delay loops]
  (let [encoder
	(doto (AnimatedGifEncoder.)
	  (.start filename)
	  (.setDelay delay)
	  (.setRepeat loops))] ;; 0 is indefinite
    (do
      (doseq
	  [img imgs]
	(if img (.addFrame encoder img)))
      (.finish encoder))))


(defn build-animated-gif
  "Build an animated gives and return the corresponding bytes array.
   Delay:  delay in milliseconds between each frame
   Repeat: number of times the gif should repeat (0 means forever)
   Frames: sequence of frames, as BufferedImage"
  [delay repeat frames]
  (let [ baos (ByteArrayOutputStream.)
         encoder (doto (com.github.gif.AnimatedGifEncoder.)
                   (.start baos)
                   (.setDelay delay)
                   (.setRepeat repeat))]
    (do
      (doseq [frame frames]
        (.addFrame encoder frame))
      (.finish encoder)
      (.flush baos)
      (let [bytes (.toByteArray baos)
            _ (.close baos)]
        bytes))))

(defn load-image [file]
  (try (javax.imageio.ImageIO/read file)
       (catch Exception _)))

(defn imgs-from-files [files]
  (map load-image files))