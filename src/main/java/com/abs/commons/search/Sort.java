package com.abs.commons.search;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hao.wang
 * @since 2016/4/27 22:52
 */
public class Sort implements Iterable<Sort.Order> {

    public static final Sort.Direction DEFAULT_DIRECTION;
    private final List<Order> orders;

    public Sort(Sort.Order... orders) {
        this(Arrays.asList(orders));
    }

    public static Sort from(Sort.Order order) {
        return new Sort(Lists.newArrayList(order));
    }

    public Sort(List<Order> orders) {
        if (null != orders && !orders.isEmpty()) {
            this.orders = orders;
        } else {
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
        }
    }

    public Sort(String... properties) {
        this(DEFAULT_DIRECTION, properties);
    }

    public Sort(Sort.Direction direction, String... properties) {
        this(direction, (properties == null ? new ArrayList() : Arrays.asList(properties)));
    }

    public Sort(Sort.Direction direction, List<String> properties) {
        if (properties != null && !properties.isEmpty()) {
            this.orders = new ArrayList(properties.size());
            Iterator var3 = properties.iterator();

            while (var3.hasNext()) {
                String property = (String) var3.next();
                this.orders.add(new Sort.Order(direction, property));
            }

        } else {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
    }

    public Sort and(Sort sort) {
        if (sort == null) {
            return this;
        } else {
            ArrayList these = new ArrayList(this.orders);
            Iterator var3 = sort.iterator();

            while (var3.hasNext()) {
                Sort.Order order = (Sort.Order) var3.next();
                these.add(order);
            }

            return new Sort(these);
        }
    }

    public Sort.Order getOrderFor(String property) {
        Iterator var2 = this.iterator();

        Sort.Order order;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            order = (Sort.Order) var2.next();
        } while (!order.getProperty().equals(property));

        return order;
    }

    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    static {
        DEFAULT_DIRECTION = Sort.Direction.ASC;
    }

    public static class Order {
        private final Sort.Direction direction;
        private final String property;

        public Order(String property) {
            this(Sort.DEFAULT_DIRECTION, property);
        }

        private Order(Sort.Direction direction, String property) {
            if (StringUtils.isBlank(property)) {
                throw new IllegalArgumentException("Property must not null or empty!");
            } else {
                this.direction = direction == null ? Sort.DEFAULT_DIRECTION : direction;
                this.property = property;
            }
        }

        public static Order from(String property, Sort.Direction direction) {
            return new Order(direction, property);
        }

        public Sort.Direction getDirection() {
            return this.direction;
        }

        public String getProperty() {
            return this.property;
        }

        public boolean isAscending() {
            return this.direction.equals(Sort.Direction.ASC);
        }

    }

    public enum Direction {
        ASC,
        DESC
    }

    private static final Joiner ORDER_JOINER = Joiner.on(",");

    public String getSqlSort() {
        StringBuilder sb = new StringBuilder();
        sb.append(" order by ");

        List<String> orders = Lists.newArrayListWithExpectedSize(this.orders.size());
        orders.addAll(this.orders.stream().map(order -> order.property + " " + order.direction).collect(Collectors.toList()));
        sb.append(ORDER_JOINER.join(orders));
        return sb.toString();
    }

}
