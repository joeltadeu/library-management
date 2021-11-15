INSERT INTO book_order (id, book_id, user_id, created_at, loaned_at, return_in)
VALUES
    (1, 1, 1,
     timestampadd('minute', 510, dateadd('month',-1, today())),
     timestampadd('minute', 510, dateadd('month',-1, today())),
     timestampadd('minute', 510, dateadd('day',-25, today()))),
    (2, 2, 1,
     timestampadd('minute', 510, dateadd('month',-1, today())),
     timestampadd('minute', 510, dateadd('month',-1, today())),
     timestampadd('minute', 510, dateadd('day',-25, today())));