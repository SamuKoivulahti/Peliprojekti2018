package fi.tamk.rikollisentunnistus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
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
        String stringPath = "";

        FileHandle[] fileArray = Gdx.files.internal("eyes").list();
        eyesTextures = new Texture[fileArray.length];

        System.out.println(fileArray.length);

        for (int i = 0; i < fileArray.length; i++) {
            stringPath = fileArray[i].path();
            eyesTextures[i] = new Texture(stringPath);
        }

        fileArray = Gdx.files.internal("noses").list();
        noseTextures = new Texture[fileArray.length];

        for (int i = 0; i < fileArray.length; i++) {
            stringPath = fileArray[i].path();
            noseTextures[i]  = new Texture(stringPath);
        }

        fileArray = Gdx.files.internal("mouths").list();
        mouthTextures = new Texture[fileArray.length];

        for (int i = 0; i < fileArray.length; i++) {
            stringPath = fileArray[i].path();
            mouthTextures[i]  = new Texture(stringPath);
        }

        fileArray = Gdx.files.internal("faceShapes").list();
        faceShapeTextures = new Texture[fileArray.length];

        for (int i = 0; i < fileArray.length; i++) {
            stringPath = fileArray[i].path();
            faceShapeTextures[i]  = new Texture(stringPath);
        }

        fileArray = Gdx.files.internal("bases").list();
        baseTextures = new Texture[fileArray.length];

        for (int i = 0; i < fileArray.length; i++) {
            stringPath = fileArray[i].path();
            baseTextures[i]  = new Texture(stringPath);
        }

        fileArray = Gdx.files.internal("hairs").list();
        hairTextures = new Texture[fileArray.length];

        for (int i = 0; i < fileArray.length; i++) {
            stringPath = fileArray[i].path();
            hairTextures[i]  = new Texture(stringPath);
        }

        fileArray = Gdx.files.internal("accessories").list();
        accessoryTextures = new Texture[fileArray.length];

        for (int i = 0; i < fileArray.length; i++) {
            stringPath = fileArray[i].path();
            accessoryTextures[i]  = new Texture(stringPath);
        }
    }

    public Face[] makeRowDifficulty(int difficultyDegree) {
        int rowLength = 5;
        int sameFeatures = 2;
        boolean accessories = false;

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

        return makeRow(rowLength,sameFeatures,accessories);
    }

    public Face[] makeRow(int rowLength, int sameFeatures, boolean accessories) {
        this.rowLength = rowLength;
        this.sameFeatures = sameFeatures;
        this.accessories = accessories;

        setSame();

        Face[] row = generateTheFaces();

        if (accessories) {
            for (int i = 1; i < row.length; i++) {
                if (MathUtils.random(0f,1f) <= 0.8f) {
                    row[i].addAccessory(accessoryTextures);
                }
            }
        }

        return row;
    }

    private void setSame() {
        boolean[] sameArray = new boolean[5];

        for (boolean value : sameArray) {
            value = false;
        }

        if (sameFeatures >= 1 && sameFeatures <= 3) {
            int sameToBeSet = sameFeatures;
            int randomPoint = -1;

            while (sameToBeSet > 0) {
                randomPoint = MathUtils.random(0, 4);

                if (sameArray[randomPoint] == false) {
                    sameArray[randomPoint] = true;
                    sameToBeSet--;
                }
            }
        } else if (sameFeatures == 4) {
            int randomPoint = MathUtils.random(1, 4);

            for (int i = 0; i <= 4; i++) {
                if (i != randomPoint) {
                    sameArray[i] = true;
                }
            }
        }

        sameShape = sameArray[0];
        sameHair = sameArray[1];
        sameEyes = sameArray[2];
        sameNose = sameArray[3];
        sameMouth = sameArray[4];
    }

    private Face[] generateTheFaces() {
        Face[] row = new Face[rowLength];

        row[0] = new Face(baseTextures, faceShapeTextures, hairTextures, eyesTextures,
                noseTextures, mouthTextures);
        int[] attributes = row[0].getFeatureIds();

        if (!sameShape && !sameHair && !sameEyes && !sameNose && !sameMouth) {
            row = addCriminalsToRow(row, attributes,false);
        } else {
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

    private Face[] addCriminalsToRow(Face[] row, int[] attributes,
                                        boolean useSameAttributes) {
        int criminalsToAdd = row.length - 1;

        while (criminalsToAdd > 0) {
            Face criminal;
            boolean sameFound = false;

            criminal = new Face(baseTextures, faceShapeTextures, hairTextures, eyesTextures,
                    noseTextures, mouthTextures, attributes, useSameAttributes);

                for (int i = 0; i < row.length; i++) {
                    if (row[i] != null) {
                        if (row[i].getIdCode().equals(criminal.getIdCode())) {
                            sameFound = true;
                        }
                    }
                }

                if (!sameFound) {
                    row[row.length - criminalsToAdd] = criminal;
                    criminalsToAdd--;
                }
            }

        return row;
    }
}
