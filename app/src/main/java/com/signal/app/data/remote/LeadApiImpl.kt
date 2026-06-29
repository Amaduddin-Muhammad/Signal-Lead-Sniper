package com.signal.app.data.remote

import com.signal.app.domain.model.Lead
import kotlin.random.Random

class LeadApiImpl : LeadApi {
    private val platforms = listOf("Facebook", "Nextdoor", "Reddit", "Twitter")
    private val snippets = mapOf(
        "Plumbing" to listOf(
            "Anyone know a good plumber? My basement is totally flooded and I need help ASAP!",
            "Need a plumber to install a new sink in my kitchen. Recommendations please!",
            "Pipe burst in my bathroom! Water spraying everywhere, need an emergency plumber right now!"
        ),
        "Auto Detailing" to listOf(
            "Looking for someone to do a deep clean and detail of my SUV before I sell it.",
            "My car is filthy after the road trip. Any good mobile detailers in the area?",
            "Need a full interior detail to get pet hair and coffee stains out of my seats."
        ),
        "Landscaping" to listOf(
            "Need my lawn mowed and bushes trimmed this week. Reliable lawn care recommendations?",
            "Looking to redesign our backyard garden. Any professional landscapers available?",
            "Tree branch fell on my fence. Need someone to clean up debris and trim the tree."
        ),
        "General Handywork" to listOf(
            "Looking for a handyman to hang some heavy shelves and assemble furniture.",
            "Need a few drywall patches fixed and painted. Recommendations?",
            "My deck has some rotten boards. Need someone to repair and stain them."
        )
    )

    private val draftTemplates = mapOf(
        "Plumbing" to listOf(
            "Hi! I saw your post about your plumbing issue. Our team at %s is fully licensed and offers 24/7 emergency service in your area. We can get a technician out to you today! Call us or reply here.",
            "Hey! If you still need a plumber, we'd love to help out at %s. We specialize in quick, quality service. Let me know if we can assist!"
        ),
        "Auto Detailing" to listOf(
            "Hey! We do mobile detailing and can come right to your driveway. Check out %s - we have premium packages to get your SUV looking brand new!",
            "Hi there! We have openings this week for deep cleans. We'll get those pet stains right out. Let me know if you'd like a quick quote!"
        ),
        "Landscaping" to listOf(
            "Hi! We handle landscaping and lawn care in your neighborhood. We'd love to take care of your lawn. Call us for a free estimate!",
            "Hey! We have a crew in your area this Thursday. We can clean up that debris and trim your tree safely. Let me know!"
        ),
        "General Handywork" to listOf(
            "Hey! I'm a local handyman and can easily help with your shelving and furniture assembly. Reasonable rates and fully insured. PM me!",
            "Hi! We can get those drywall patches fixed up and painted to match perfectly. Let me know when you're free for a quick visit."
        )
    )

    override suspend fun fetchLeads(category: String, radius: Float): List<Lead> {
        val selectedCategory = if (category.isEmpty()) "Plumbing" else category
        val pool = snippets[selectedCategory] ?: snippets["Plumbing"]!!
        val drafts = draftTemplates[selectedCategory] ?: draftTemplates["Plumbing"]!!
        
        val random = Random(System.currentTimeMillis())
        
        return List(3) { index ->
            val id = "lead_${System.currentTimeMillis()}_$index"
            val platform = platforms.random(random)
            val dist = "${random.nextInt(1, radius.toInt().coerceAtLeast(5))} miles away"
            val score = if (random.nextBoolean()) "HIGH" else "MEDIUM"
            val snippet = pool.random(random)
            val draft = String.format(drafts.random(random), "Signal B2B")
            
            Lead(
                id = id,
                platform = platform,
                timeAgo = "${random.nextInt(5, 59)} mins ago",
                distance = dist,
                intentScore = score,
                postSnippet = snippet,
                aiDraftReply = draft,
                status = "NEW"
            )
        }
    }
}
