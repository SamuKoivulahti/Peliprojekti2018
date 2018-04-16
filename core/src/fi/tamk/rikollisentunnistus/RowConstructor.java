package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Essi Supponen on 16/03/2018.
 */

public class RowConstructor {
    private int rowLength;          // 3 - 6 people
    private int sameFeatures;       // 0 - 4 same features
    private boolean accessories;

    private boolean sameShape;
    private boolean sameHair;
    private boolean sameEyes;
    private boolean sameNose;
    private boolean sameMouth;

    Texture[] baseTextures;
    Texture[] faceShapeTextures;
    Texture[] hairTextures;
    Texture[] eyesTextures;
    Texture[] noseTextures;
    Texture[] mouthTextures;

    Texture[] accessoryTextures;

    public RowConstructor() {
        faceShapeTextures = filesToTextures("faceShapes", "face%02d.png");
        hairTextures = filesToTextures("hairs", "hair%02d.png");
        accessoryTextures = filesToTextures("accessories", "accessory%02d.png");
        mouthTextures = filesToTextures("mouths", "mouth%02d.png");
        eyesTextures = filesToTextures("eyes", "eyes%02d.png");
        noseTextures = filesToTextures("noses", "nose%02d.png");

        baseTextures = new Texture[3];
        baseTextures[0] = new Texture("bases/base01.png");
        baseTextures[1] = new Texture("bases/base02.png");
        baseTextures[2] = new Texture("bases/base03.png");
    }


    /**
     * Creates the textureRows from asset directories.
     */
    private Texture[] filesToTextures(String directoryPath, String fileMask) {
        FileHandle[] files = Gdx.files.internal(directoryPath).list();

        if (files != null && files.length > 0) {
            Texture[] textures = new Texture[files.length];

            for (int i = 0; i < files.length; i++) {
                String fileName = String.format(fileMask, i + 1);
                // Gdx.app.log(fileName, "" +Gdx.files.internal(directoryPath).child(fileName).exists());
                if (Gdx.files.internal(directoryPath).child(fileName).exists()) {
                    textures[i] = new Texture(files[i].path());
                }
            }

            return textures;
        }

        return null;
    }

    /**
     * Assembles a row of Face objects by given difficulty.
     *
     * @param difficultyDegree
     * @return row of Face objects
     */
    public Face[] makeRowDifficulty(int difficultyDegree) {
        /**
         * Default values if there happens any kind of bug.
         */
        int rowLength = 5;
        int sameFeatures = 2;
        boolean accessories = false;

        /**
         * Sets the rowLenght, sameFeatures and accessories values.
         */
        if (difficultyDegree == 0) {
            rowLength = 3;
            sameFeatures = 0;
            accessories = false;
        } else if (difficultyDegree > 0 && difficultyDegree < 6) {
            rowLength = 4;
            if (difficultyDegree == 1) {
                sameFeatures = 0;
                accessories = false;
            } else if (difficultyDegree == 2) {
                sameFeatures = 1;
                accessories = false;
            } else if (difficultyDegree == 3) {
                sameFeatures = 2;
                accessories = false;
            } else if (difficultyDegree == 4) {
                sameFeatures = 2;
                accessories = true;
            } else {
                sameFeatures = 3;
                accessories = true;
            }
        } else if (difficultyDegree >= 6 && difficultyDegree <= 10) {
            rowLength = 5;

            if (difficultyDegree == 6) {
                sameFeatures = 1;
                accessories = false;
            } else if (difficultyDegree == 7) {
                sameFeatures = 2;
                accessories = false;
            } else if (difficultyDegree == 8) {
                sameFeatures = 2;
                accessories = true;
            } else if (difficultyDegree == 9) {
                sameFeatures = 3;
                accessories = true;
            } else {
                sameFeatures = 4;
                accessories = false;
            }
        } else if (difficultyDegree >= 11 && difficultyDegree <= 14) {
            rowLength = 6;

            if (difficultyDegree == 11) {
                sameFeatures = 2;
                accessories = false;
            } else if (difficultyDegree == 12) {
                sameFeatures = 3;
                accessories = false;
            } else if (difficultyDegree == 13) {
                sameFeatures = 2;
                accessories = true;
            } else {
                sameFeatures = 3;
                accessories = true;
            }
        }

        /**
         * Assembles and returns a row with the values.
         */
        return makeRow(rowLength,sameFeatures,accessories);
    }

    /**
     * Assembles a row of Face objects by given values.
     *
     * @param rowLength
     * @param sameFeatures
     * @param accessories
     * @return row of Face objects
     */
    public Face[] makeRow(int rowLength, int sameFeatures, boolean accessories) {
        /**
         * Sets given values to class variables.
         */
        this.rowLength = rowLength;
        this.sameFeatures = sameFeatures;
        this.accessories = accessories;

        /**
         * Calls setSame method.
         */
        setSame();

        /**
         * Calls generateTheFaces method.
         */
        Face[] row = generateTheFaces();

        /**
         * If accessories is true, gives all other faces than the right criminal (first object
         * of the array) an accessory with 80% chance.
         */
        if (accessories) {
            for (int i = 1; i < row.length; i++) {
                if (MathUtils.random(0f,1f) <= 0.8f) {
                    row[i].addAccessory(accessoryTextures);
                }
            }
        }

        return row;
    }

    /**
     * Randomly decides which features are same with every Face in a row.
     */
    private void setSame() {
        boolean[] sameArray = new boolean[5];

        if (sameFeatures >= 1 && sameFeatures <= 3) {
            /**
             * Generates random numbers and changes those values in sameArray into true.
             */
            int sameToBeSet = sameFeatures;
            int randomPoint;

            while (sameToBeSet > 0) {
                randomPoint = MathUtils.random(0, 4);

                if (sameArray[randomPoint] == false) {
                    sameArray[randomPoint] = true;
                    sameToBeSet--;
                }
            }
        } else if (sameFeatures == 4) {
            /**
             * When there is four same features, faceshape will always be same.
             */
            int randomPoint = MathUtils.random(1, 4);

            for (int i = 0; i <= 4; i++) {
                if (i != randomPoint) {
                    sameArray[i] = true;
                }
            }
        }

        /**
         * Sets the values from sameArray into class variables.
         */
        sameShape = sameArray[0];
        sameHair = sameArray[1];
        sameEyes = sameArray[2];
        sameNose = sameArray[3];
        sameMouth = sameArray[4];
    }

    /**
     * Generates a row of Face objects according class variables.
     *
     * @return a row of Face objects
     */
    private Face[] generateTheFaces() {
        /**
         * Generates an array of Faces with lenght of  rowLenght;
         */
        Face[] row = new Face[rowLength];

        /**
         * The first object in row will be the right criminal. Takes attributes of that object
         * to use when generating the other objects.
         */
        row[0] = new Face(baseTextures, faceShapeTextures, hairTextures, eyesTextures,
                noseTextures, mouthTextures);
        int[] attributes = row[0].getFeatureIds();

        if (!sameShape && !sameHair && !sameEyes && !sameNose && !sameMouth) {
            row = addCriminalsToRow(row, attributes,false);
        } else {
            /**
             * Changes the attributes into -1 if it can be any of the available features.
             */
            if (!sameShape) {
                attributes[0] = -1;
            }
            if (!sameHair) {
                attributes[1] = -1;
            }
            if (!sameEyes) {
                attributes[2] = -1;
            }
            if (!sameNose) {
                attributes[3] = -1;
            }
            if (!sameMouth) {
                attributes[4] = -1;
            }

            row = addCriminalsToRow(row, attributes,true);
        }

        return row;
    }

    /**
     * Adds Face objects to a given row according given attributes.
     *
     * @param row                   the row of criminals that needs to be filled
     * @param attributes            facial features of the right criminal
     * @param useSameAttributes     true if even one features will be the same in every face
     * @return                      a row full of Face objects
     */
    private Face[] addCriminalsToRow(Face[] row, int[] attributes,
                                        boolean useSameAttributes) {
        int criminalsToAdd = row.length - 1;

        while (criminalsToAdd > 0) {
            Face criminal;
            boolean sameFound = false;

            /**
             * generates a Face object
             */
            criminal = new Face(baseTextures, faceShapeTextures, hairTextures, eyesTextures,
                    noseTextures, mouthTextures, attributes, useSameAttributes);

            /**
             * Checks if any of the objects in the row is exactly same than the generated object.
             */
            for (int i = 0; i < row.length; i++) {
                if (row[i] != null) {
                    if (row[i].getIdCode().equals(criminal.getIdCode())) {
                        sameFound = true;}
                }
            }

            /**
             * If none of the objects in the array is exactly same, add object to the array.
             */
            if (!sameFound) {
                row[row.length - criminalsToAdd] = criminal;
                criminalsToAdd--;
            }
        }

        return row;
    }

    public boolean getAccessories() {
        return accessories;
    }
}
