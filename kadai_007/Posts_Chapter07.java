package kadai_007;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Posts_Chapter07 {

	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement statement = null;

		// 投稿リスト
		String[][] postsList = {
			{"1003", "2023-02-08", "昨日の夜は徹夜でした", "13"},
			{"1002", "2023-02-08", "お疲れ様です！", "12"},
		    {"1003", "2023-02-09", "今日も頑張ります！", "18"},
            {"1001", "2023-02-09", "無理は禁物ですよ！", "17"},
            {"1002", "2023-02-10", "明日から連休ですね！", "20"}
		};

		try {
			// データベースに接続
			con = DriverManager.getConnection(
				"jdbc:mysql://localhost/challenge_java",
				"root",
				"root"
			);
			System.out.println("データベース接続成功：");

			// SQLクエリを準備(INSERT)
			String sql = "INSERT INTO posts (user_id, posted_at, post_content, likes) VALUES (?, ?, ?, ?);";
			statement = con.prepareStatement(sql);

			System.out.println("レコード追加を実行します");
			
			// リストの1行目から順番に読み込む
			int rowCnt = 0;
			for (int i = 0; i < postsList.length; i++) {
				// SQLクエリの「?」部分をリストのデータに置き換え
				statement.setString(1, postsList[i][0]); // ユーザーID
				statement.setString(2, postsList[i][1]); // 投稿日時
				statement.setString(3, postsList[i][2]); // 投稿内容
				statement.setString(4, postsList[i][3]); // いいね数

				// SQLクエリを実行（DBMSに送信）
				rowCnt += statement.executeUpdate();
			}
			System.out.println(rowCnt + "件のレコードが追加されました");

			// SQLクエリを準備(SELECT)
            sql = "SELECT posted_at, post_content, likes FROM posts WHERE user_id = 1002 ORDER BY posted_at;";

			// SQLクエリを実行（DBMSに送信
			ResultSet result = statement.executeQuery(sql);
			System.out.println("ユーザーIDが1002のレコードを検索しました");

			// SQLクエリの実行結果を抽出
			rowCnt = 0;
			while(result.next()) {
				rowCnt++;
				String posted_at = result.getString("posted_at");
				String post_content = result.getString("post_content");
				Integer likes = result.getInt("likes");
				System.out.println(rowCnt + "件目：投稿日時=" + posted_at + "／投稿内容=" + post_content + "／いいね数=" + likes);
			}
		} catch(SQLException e) {
			System.out.println("エラー発生：" + e.getMessage());
		} finally {
				if (statement != null) {
					try {statement.close();} catch(SQLException ignore) {}
				}
				if (con != null) {
					try {con.close();} catch(SQLException ignore) {}
				}
			}
	}
}