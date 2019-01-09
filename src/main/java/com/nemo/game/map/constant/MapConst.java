package com.nemo.game.map.constant;

/**
 * Created by h on 2018/8/12.
 */
public interface MapConst {

    //速度
    interface Speed {
        int WALK = 240;
        int RUN = 455;
        int HORSE = 460;
    }

    //方向量
    enum Dir{
        // 同一个点
        NONE(0, -1) {
            @Override
            public Dir left() {
                return NONE;
            }

            @Override
            public Dir right() {
                return NONE;
            }

            @Override
            public Dir behind() {
                return NONE;
            }
        },
        // 上
        TOP(0x10000000, 0) {
            @Override
            public Dir left() {
                return LEFT_TOP;
            }

            @Override
            public Dir right() {
                return RIGHT_TOP;
            }

            @Override
            public Dir behind() {
                return BOTTOM;
            }
        },
        RIGHT_TOP(0x01000000, 1) {// 右上
            @Override
            public Dir left() {
                return TOP;
            }

            @Override
            public Dir right() {
                return RIGHT;
            }

            @Override
            public Dir behind() {
                return LEFT_BOTTOM;
            }
        },
        RIGHT(0x00100000, 2) {// 右
            @Override
            public Dir left() {
                return RIGHT_TOP;
            }

            @Override
            public Dir right() {
                return RIGHT_BOTTOM;
            }

            @Override
            public Dir behind() {
                return LEFT;
            }
        },
        RIGHT_BOTTOM(0x00010000, 3) {// 右下
            @Override
            public Dir left() {
                return RIGHT;
            }

            @Override
            public Dir right() {
                return BOTTOM;
            }

            @Override
            public Dir behind() {
                return LEFT_TOP;
            }
        },
        BOTTOM(0x00001000, 4) {// 下
            @Override
            public Dir left() {
                return RIGHT_BOTTOM;
            }

            @Override
            public Dir right() {
                return LEFT_BOTTOM;
            }

            @Override
            public Dir behind() {
                return TOP;
            }
        },
        LEFT_BOTTOM(0x00000100, 5) {// 左下
            @Override
            public Dir left() {
                return BOTTOM;
            }

            @Override
            public Dir right() {
                return LEFT;
            }

            @Override
            public Dir behind() {
                return RIGHT_TOP;
            }
        },
        LEFT(0x00000010, 6) {// 左
            @Override
            public Dir left() {
                return LEFT_BOTTOM;
            }

            @Override
            public Dir right() {
                return LEFT_TOP;
            }

            @Override
            public Dir behind() {
                return RIGHT;
            }
        },
        LEFT_TOP(0x00000001, 7) {// 左上
            @Override
            public Dir left() {
                return LEFT;
            }

            @Override
            public Dir right() {
                return TOP;
            }

            @Override
            public Dir behind() {
                return RIGHT_BOTTOM;
            }
        },;
        //方向值
        private int value;
        //八叉树数组中的索引位置 0到7
        private int index;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        //左侧方向（逆时针）
        public abstract Dir left();
        //右侧方向（顺时针）
        public abstract Dir right();
        //相反方向（对面）
        public abstract Dir behind();

        public static Dir getByIndex(int index) {
            for(Dir dir : values()) {
                if(dir.index == index) {
                    return dir;
                }
            }
            return null;
        }

        private Dir(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }



}
