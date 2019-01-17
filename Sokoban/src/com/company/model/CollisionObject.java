package com.company.model;

// отвечает за столкновение объектов. они не должны сталкиваться.
public abstract class CollisionObject extends GameObject {

    //constructor
    public CollisionObject(int x, int y) {
        super(x, y);
    }


    public boolean isCollision(GameObject gameObject, Direction direction) {

        boolean result = false;

        switch (direction) {

            case LEFT:
                if (getX() - Model.FIELD_SELL_SIZE == gameObject.getX() && getY() == gameObject.getY())
                    result = true;
                break;
            case RIGHT:
                if (getX() + Model.FIELD_SELL_SIZE == gameObject.getX() && getY() == gameObject.getY())
                    result = true;
                break;
            case UP:
                if (getX() == gameObject.getX() && getY() - Model.FIELD_SELL_SIZE == gameObject.getY())
                    result = true;
                break;
            case DOWN:
                if (getX() == gameObject.getX() && getY() + Model.FIELD_SELL_SIZE == gameObject.getY())
                    result = true;
                break;
        }
        return result;
    }
}
