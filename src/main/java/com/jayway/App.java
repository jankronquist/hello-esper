package com.jayway;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class App {
    public static void main(String[] args) {
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
        String expression = "select avg(price) from " + OrderEvent.class.getName() + ".win:time(30 sec)";
        EPStatement statement = epService.getEPAdministrator().createEPL(expression);
        MyListener listener = new MyListener();
        statement.addListener(listener);
        OrderEvent event = new OrderEvent("shirt", 100.0);
        epService.getEPRuntime().sendEvent(event);

    }

    public static class MyListener implements UpdateListener {
        public void update(EventBean[] newEvents, EventBean[] oldEvents) {
            EventBean event = newEvents[0];
            System.out.println("avg=" + event.get("avg(price)"));
        }
    }

    public static class OrderEvent {
        private String itemName;
        private double price;

        public OrderEvent(String itemName, double price) {
            this.itemName = itemName;
            this.price = price;
        }

        public String getItemName() {
            return itemName;
        }

        public double getPrice() {
            return price;
        }
    }

}
