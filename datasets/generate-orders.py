"""
Generate synthetic order data for performance testing.

Usage:
    python generate-orders.py --count 100000 --output orders-100k.csv

Output format:
    orderId,symbol,side,price,quantity
    ORD00001,AAPL,BUY,150.50,100
"""

import argparse
import random
import csv
from datetime import datetime

SYMBOLS = ['AAPL', 'GOOG', 'MSFT', 'AMZN', 'META', 'NVDA', 'TSLA', 'JPM', 'V', 'WMT']
SIDES = ['BUY', 'SELL']

# Price ranges per symbol (for realistic data)
PRICE_RANGES = {
    'AAPL': (170, 200),
    'GOOG': (130, 160),
    'MSFT': (350, 420),
    'AMZN': (140, 180),
    'META': (350, 450),
    'NVDA': (450, 550),
    'TSLA': (180, 280),
    'JPM': (150, 200),
    'V': (250, 300),
    'WMT': (150, 180),
}

def generate_order(order_id: int) -> dict:
    """Generate a single random order."""
    symbol = random.choice(SYMBOLS)
    price_range = PRICE_RANGES[symbol]
    
    return {
        'orderId': f'ORD{order_id:08d}',
        'symbol': symbol,
        'side': random.choice(SIDES),
        'price': round(random.uniform(*price_range), 2),
        'quantity': random.randint(10, 1000)
    }

def main():
    parser = argparse.ArgumentParser(description='Generate synthetic order data')
    parser.add_argument('--count', type=int, default=10000, help='Number of orders to generate')
    parser.add_argument('--output', type=str, default='orders.csv', help='Output file path')
    parser.add_argument('--seed', type=int, default=42, help='Random seed for reproducibility')
    args = parser.parse_args()
    
    random.seed(args.seed)
    
    print(f'Generating {args.count:,} orders...')
    start_time = datetime.now()
    
    with open(args.output, 'w', newline='') as f:
        writer = csv.DictWriter(f, fieldnames=['orderId', 'symbol', 'side', 'price', 'quantity'])
        writer.writeheader()
        
        for i in range(1, args.count + 1):
            order = generate_order(i)
            writer.writerow(order)
            
            if i % 100000 == 0:
                print(f'  Generated {i:,} orders...')
    
    elapsed = (datetime.now() - start_time).total_seconds()
    print(f'Done! Generated {args.count:,} orders in {elapsed:.2f}s')
    print(f'Output: {args.output}')

if __name__ == '__main__':
    main()
